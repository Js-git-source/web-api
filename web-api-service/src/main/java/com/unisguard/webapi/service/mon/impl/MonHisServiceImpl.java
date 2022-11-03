package com.unisguard.webapi.service.mon.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.mon.MonHisDO;
import com.unisguard.webapi.manage.mon.MonHisManage;
import com.unisguard.webapi.service.mon.MonHisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonHisServiceImpl implements MonHisService {

    @Resource
    private MonHisManage monHisManage;

    @Override
    public ResponseDO<List<MonHisDO>> middlewareDetailList(MonHisDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.MIDDLEWARE);
        param.setQuota(DictConstant.SYSTEM_MONITOR_TOPIC);
        List<MonHisDO> monHisDOList = monHisManage.middlewareDetailList(param);
        return ResponseDO.success(monHisDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Map<String, List<MonHisDO>>> analysisFlow(MonHisDO param) {
        param.setIp(param.getIp());
        param.setCategory(DictConstant.SERVICE_NODE);
        param.setType(DictConstant.ANALYSIS_CENTER);
        param.setQuota(DictConstant.SYSTEM_MONITOR_NETWORK);
        List<MonHisDO> result = monHisManage.analysisFlow(param);
        Map<String, List<MonHisDO>> resultMap = result.stream().collect(Collectors.groupingBy(MonHisDO::getNetInter));
        return ResponseDO.success(resultMap);
    }
}