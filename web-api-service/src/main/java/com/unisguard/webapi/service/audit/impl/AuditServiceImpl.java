package com.unisguard.webapi.service.audit.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.constant.LogConstant;
import com.unisguard.webapi.common.dataobject.audit.AuditDO;
import com.unisguard.webapi.common.dataobject.audit.AuditExcelDO;
import com.unisguard.webapi.common.dataobject.audit.AuditLogDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.DictUtil;
import com.unisguard.webapi.common.util.LocalDateTimeUtil;
import com.unisguard.webapi.manage.common.CommonManage;
import com.unisguard.webapi.manage.user.UserManage;
import com.unisguard.webapi.service.audit.AuditService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuditServiceImpl implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(LogConstant.SYS);

    @Resource
    private CommonManage commonManage;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private UserManage userManage;

    @Override
    public ResponseDO<List<AuditDO>> list(AuditDO param) {
        // ??????????????????
        SearchSourceBuilder sourceBuilder = handleParam(param);
        // ???????????????...??????
        sourceBuilder.from((param.getPage() - 1) * param.getLimit());
        sourceBuilder.size(param.getLimit());
        SearchResponse search = search(sourceBuilder);
        long total = search.getHits().getTotalHits().value;
        if (total <= 0) {
            return ResponseDO.success(new ArrayList<>());
        }
        List<AuditDO> list = new ArrayList<>();
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            AuditDO auditDO = JSON.parseObject(JSON.toJSONString(map), AuditDO.class);
            if (StringUtils.isNotBlank(auditDO.getUserName()) && StringUtils.isNotBlank(auditDO.getUserAcc())) {
                auditDO.setUserName(auditDO.getUserName() + "(" + auditDO.getUserAcc() + ")");
            } else {
                auditDO.setUserName("");
            }
            auditDO.setEsId(hit.getId());
            list.add(auditDO);
        }
        return ResponseDO.success(list, total);
    }

    /**
     * @param sourceBuilder ????????????
     * @return ????????????
     */
    private SearchResponse search(SearchSourceBuilder sourceBuilder) {
        try {
            SearchRequest searchRequest = new SearchRequest(GlobalConstant.INDEX_SYS_AUDIT);
            // ?????????????????????
            sourceBuilder.timeout(new TimeValue(GlobalConstant.SIXTY, TimeUnit.SECONDS));
            // ???????????????????????????????????????
            searchRequest.source(sourceBuilder);
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (search.status().getStatus() != 200) {
                throw new ApplicationException(GlobalConstant.VISIT_ES_ERROR);
            }
            return search;
        } catch (Exception e) {
            log.error("method:search, ??????????????????ES?????????", e);
            throw new ApplicationException(GlobalConstant.VISIT_ES_ERROR);
        }
    }


    @Override
    public ResponseDO<AuditDO> detail(String esId) {
        // ??????????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // ????????????  ??????id??????
        sourceBuilder.query(QueryBuilders.termsQuery("_id", esId));
        SearchResponse search = search(sourceBuilder);
        // ???????????????????????????????????????data???null
        if (search.getHits().getTotalHits().value <= 0) {
            return ResponseDO.success();
        }
        SearchHit[] hits = search.getHits().getHits();
        if (hits.length <= 0) {
            return ResponseDO.success();
        }
        Map<String, Object> map = hits[0].getSourceAsMap();
        AuditDO auditDO = JSON.parseObject(JSON.toJSONString(map), AuditDO.class);
        return ResponseDO.success(auditDO);
    }

    @Override
    public void add(AuditDO param) throws Exception {
        AuditLogDO auditLog = new AuditLogDO(param);
        auditLog.setData_id(commonManage.getUUID());
        IndexRequest request = new IndexRequest(GlobalConstant.INDEX_SYS_AUDIT);
        request.source(JSON.toJSONString(auditLog), XContentType.JSON);
        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    @Override
    public ResponseDO download(OutputStream outputStream, AuditDO param) {
        EasyExcel.write(outputStream, AuditExcelDO.class).sheet(GlobalConstant.MODULE_AUDIT_NAME).doWrite(getExcelAuditList(param));
        return ResponseDO.success();
    }

    @Override
    public ResponseDO clear(AuditDO param) throws Exception {
        // ??????????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // ???????????????????????????
        sourceBuilder.trackTotalHits(true);
        // ????????????100?????????
        sourceBuilder.size(GlobalConstant.HUNDRED);
        SearchResponse search = search(sourceBuilder);
        long total = search.getHits().getTotalHits().value;
        while (total > 0) {
            List<String> ids = getIds(search);
            BulkRequest request = new BulkRequest();
            for (String id : ids) {
                DeleteRequest deleteRequest = new DeleteRequest(GlobalConstant.INDEX_SYS_AUDIT, id);
                request.add(deleteRequest);
            }
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            search = search(sourceBuilder);
            total = search.getHits().getTotalHits().value;
        }
        return ResponseDO.success();
    }

    private List<String> getIds(SearchResponse search) {
        if (search.getHits().getTotalHits().value <= 0) {
            return new ArrayList<>();
        }
        List<String> ids = new ArrayList<>();
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            ids.add(hit.getId());
        }
        return ids;
    }

    /**
     * @param param ??????????????????
     * @return ?????????????????????????????????
     */
    private List<AuditExcelDO> getExcelAuditList(AuditDO param) {
        // ????????????
        SearchSourceBuilder sourceBuilder = handleParam(param);
        sourceBuilder.from(0);
        // ?????????????????????
        sourceBuilder.size(GlobalConstant.THOUSAND);
        SearchResponse search = search(sourceBuilder);
        long total = search.getHits().getTotalHits().value;
        if (total <= 0) {
            return new ArrayList<>();
        }
        List<AuditExcelDO> list = new ArrayList<>();
        long page = total % GlobalConstant.THOUSAND == 0 ? total / GlobalConstant.THOUSAND : total / GlobalConstant.THOUSAND + 1;
        int num = 1;
        while (page > 0) {
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> map = hit.getSourceAsMap();
                AuditExcelDO auditDO = JSON.parseObject(JSON.toJSONString(map), AuditExcelDO.class);
                //?????????
                if (StringUtils.isNotBlank(auditDO.getUserName()) && StringUtils.isNotBlank(auditDO.getUserAcc())) {
                    auditDO.setUserName(auditDO.getUserName() + "(" + auditDO.getUserAcc() + ")");
                } else {
                    auditDO.setUserName("");
                }
                auditDO.setModuleIdName(DictUtil.getNameByCode(auditDO.getModuleId()));
                auditDO.setOpTypeName(DictUtil.getNameByCode(auditDO.getOpType()));
                auditDO.setOpRetName(DictUtil.getNameByCode(auditDO.getOpRet()));
                auditDO.setMsg("??????" + auditDO.getUserName() + "???[" + auditDO.getModuleIdName() +
                        "]???????????????" + auditDO.getOpTypeName() + "???????????????????????????" + auditDO.getOpRetName());
                list.add(auditDO);
            }
            page--;
            if (page == 0) {
                break;
            }
            sourceBuilder.trackTotalHits(true);
            sourceBuilder.from(num * GlobalConstant.THOUSAND);
            // ?????????????????????
            sourceBuilder.size(GlobalConstant.THOUSAND);
            search = search(sourceBuilder);
            num++;
        }
        return list;
    }

    /**
     * @param param ??????????????????
     * @return ??????????????????
     */
    private SearchSourceBuilder handleParam(AuditDO param) {
        // ??????????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // ?????????????????????????????????QueryBuilders ??????????????????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // ????????????
        if (param.getModuleId() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("module_id", param.getModuleId()));
        }
        // ????????????
        if (StringUtils.isNotBlank(param.getOptTimeStr())) {
            String[] array = param.getOptTimeStr().split("~");
            if (array.length == 2) {
                String from = array[0] + " 00:00:00";
                String to = array[1] + " 23:59:59";
                boolQueryBuilder.must(QueryBuilders.rangeQuery("op_time").gt(LocalDateTimeUtil.getTimeFromDateStr(from)).lt(LocalDateTimeUtil.getTimeFromDateStr(to)));
            }
        }
        // ????????????
        if (param.getOpType() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("op_type", param.getOpType()));
        }
        // ????????????
        if (param.getOpRet() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("op_ret", param.getOpRet()));
        }
        // ????????????????????????????????????
        if (StringUtils.isNotBlank(param.getUserName())) {
            //??????in ??????
            List<Long> userIds = userManage.getUserId(param.getUserName());
            if (CollectionUtils.isEmpty(userIds)) {
                boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("user_id", 0));
            } else {
                for (int j = 0; j < userIds.size(); j++) {
                    boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("user_id", userIds.get(j)));
                }
            }
        }
        // ???IP??????
        if (StringUtils.isNotBlank(param.getClientIp())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("client_ip", param.getClientIp()));
        }
        // ????????????????????????????????????
        sourceBuilder.sort("op_time", "asc".equals(param.getSortOrder()) ? SortOrder.ASC : SortOrder.DESC);
        // ???????????????????????????
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.query(boolQueryBuilder);
        return sourceBuilder;
    }
}
