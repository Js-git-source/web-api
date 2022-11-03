package com.unisguard.webapi.service.mon.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.mon.MonDO;
import com.unisguard.webapi.common.dataobject.mon.MonHisDO;
import com.unisguard.webapi.common.util.LocalDateTimeUtil;
import com.unisguard.webapi.manage.mon.MonHisManage;
import com.unisguard.webapi.manage.mon.MonManage;
import com.unisguard.webapi.service.mon.MonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonServiceImpl implements MonService {
    @Resource
    private MonManage monManage;
    @Resource
    private MonHisManage monHisManage;

    @Override
    public ResponseDO<List<MonDO>> list(MonDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<MonDO> monDOList = monManage.list(param);
        return ResponseDO.success(monDOList, page.getTotal());
    }

    @Override
    public ResponseDO<MonDO> serviceList(MonDO param) {
        param.setCategory(DictConstant.SERVICE_NODE);
        param.setType(DictConstant.ANALYSIS_CENTER);
        param = monManage.detail(param);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> cpu(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.WEB_MIDDLEWARE);
        param = monManage.detail(param);
        if (param == null) {
            return ResponseDO.success(param);
        }
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.WEB_MIDDLEWARE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_CPU);
        List<MonHisDO> cpuMonHisList = monHisManage.cpu(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),cpuMonHisList);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> ram(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.WEB_MIDDLEWARE);
        param = monManage.detail(param);
        if (param == null) {
            return ResponseDO.success(param);
        }
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.WEB_MIDDLEWARE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_RAM);
        List<MonHisDO> ramMonHisList = monHisManage.ram(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),ramMonHisList);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> thread(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.WEB_MIDDLEWARE);
        param = monManage.detail(param);
        if (param == null) {
            return ResponseDO.success(param);
        }
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.WEB_MIDDLEWARE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_THREAD);
        List<MonHisDO> threadMonHisList = monHisManage.thread(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),threadMonHisList);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> connect(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.WEB_MIDDLEWARE);
        param = monManage.detail(param);
        if (param == null) {
            return ResponseDO.success(param);
        }
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.WEB_MIDDLEWARE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_CONNECT);
        List<MonHisDO> connectMonHisList = monHisManage.connect(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),connectMonHisList);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> databaseConnect(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.DATABASE);
        param = monManage.detail(param);
        if (param == null) {
            return ResponseDO.success(param);
        }
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.DATABASE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_CONNECT);
        List<MonHisDO> databaseConnect = monHisManage.databaseConnect(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),databaseConnect);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> throughput(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.DATABASE);
        param = monManage.detail(param);
        if (param == null) {
            return ResponseDO.success(param);
        }
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.DATABASE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_THROUGHPUT);
        List<MonHisDO> throughputMonHisList = monHisManage.throughput(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),throughputMonHisList);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> io(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.DATABASE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_IO);
        List<MonHisDO> ioMonHisList = monHisManage.io(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),ioMonHisList);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> network(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.DATABASE);
        param = monManage.detail(param);
        if (param == null) {
            return ResponseDO.success(param);
        }
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.DATABASE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_NETWORK);
        List<MonHisDO> networkList = monHisManage.network(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),networkList);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> search(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.INDEX);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_INDEX);
        List<MonHisDO> result = monHisManage.search(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),result);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<List<MonDO>> getEngineList(MonDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        param.setCategory(DictConstant.ENGINE_SERVICE);
        param.setType(DictConstant.ENGINE_TYPE_TIME_TASK);
        List<MonDO> monDOList = monManage.getEngineList(param);
        return ResponseDO.success(monDOList, page.getTotal());
    }

    @Override
    public ResponseDO<List<MonDO>> indexList(MonDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.INDEX);
        List<MonDO> monDOList = monManage.indexList(param);
        return ResponseDO.success(monDOList, page.getTotal());
    }

    @Override
    public ResponseDO<MonDO> redisDetail(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.WEB_MIDDLEWARE);
        param = monManage.detail(param);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<List<MonDO>> middlewareList(MonDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.MIDDLEWARE);
        List<MonDO> monDOList = monManage.middlewareList(param);
        return ResponseDO.success(monDOList, page.getTotal());
    }

    @Override
    public ResponseDO<MonDO> usageCpu(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.CACHE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_CPU);
        List<MonHisDO> result = monHisManage.usageCpu(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),result);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> usageRam(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.CACHE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_RAM);
        List<MonHisDO> result = monHisManage.usageRam(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),result);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> key(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.CACHE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_KEY);
        List<MonHisDO> result = monHisManage.key(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),result);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<List<MonDO>> topic(MonDO param) {
        param.setCategory(DictConstant.CORE_COMPONENT);
        param.setType(DictConstant.MIDDLEWARE);
        List<MonDO> result = monManage.list(param);
        return ResponseDO.success(result);
    }

    @Override
    public ResponseDO<MonDO> flow(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.CORE_COMPONENT);
        monHisDO.setType(DictConstant.MIDDLEWARE);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_TOPIC);
        monHisDO.setTopic(param.getTopic());
        monHisDO.setGroupId(param.getGroupId());
        List<MonHisDO> result = monHisManage.flow(monHisDO);
        Map<String, List<MonHisDO>> resultMap = result.stream().collect(Collectors.groupingBy(item -> item.getTopic()+":"+item.getGroupId()));
        List<List<MonHisDO>> monHisLists = new ArrayList<>(resultMap.values());
        param.setMonHisDOLists(monHisLists);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> analysisUsageCpu(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.SERVICE_NODE);
        monHisDO.setType(DictConstant.ANALYSIS_CENTER);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_CPU);
        List<MonHisDO> result = monHisManage.analysisUsageCpu(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),result);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> analysisUsageRam(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.SERVICE_NODE);
        monHisDO.setType(DictConstant.ANALYSIS_CENTER);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_CPU);
        List<MonHisDO> result = monHisManage.analysisUsageRam(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),result);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

    @Override
    public ResponseDO<MonDO> analysisLoad(MonDO param) {
        MonHisDO monHisDO = new MonHisDO();
        monHisDO.setIp(param.getIp());
        monHisDO.setCategory(DictConstant.SERVICE_NODE);
        monHisDO.setType(DictConstant.ANALYSIS_CENTER);
        monHisDO.setQuota(DictConstant.SYSTEM_MONITOR_CPU);
        List<MonHisDO> result = monHisManage.analysisLoad(monHisDO);
        List<MonHisDO> resultList = LocalDateTimeUtil.allMonHisList(13,monHisDO.getEndTime(),result);
        param.setMonHisDOList(resultList);
        return ResponseDO.success(param);
    }

}