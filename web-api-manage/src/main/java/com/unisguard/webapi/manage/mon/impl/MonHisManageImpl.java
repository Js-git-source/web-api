package com.unisguard.webapi.manage.mon.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.mon.MonHisDO;
import com.unisguard.webapi.manage.mon.MonHisManage;
import com.unisguard.webapi.mapper.mon.MonHisMapper;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class MonHisManageImpl implements MonHisManage {
    @Resource
    private MonHisMapper monHisMapper;

    @Override
    public List<MonHisDO> list(MonHisDO param) {
        return monHisMapper.list(param);
    }

    @Override
    public MonHisDO detail(Long id) {
        return monHisMapper.detail(id);
    }

    @Override
    public List<MonHisDO> cpu(MonHisDO param) {
        return monHisMapper.cpu(param);
    }

    @Override
    public List<MonHisDO> ram(MonHisDO param) {
        return monHisMapper.ram(param);
    }

    @Override
    public List<MonHisDO> thread(MonHisDO param) {
        return monHisMapper.thread(param);
    }

    @Override
    public List<MonHisDO> connect(MonHisDO param) {
        return monHisMapper.connect(param);
    }

    @Override
    public List<MonHisDO> databaseConnect(MonHisDO param) {
        return monHisMapper.databaseConnect(param);
    }

    @Override
    public List<MonHisDO> throughput(MonHisDO param) {
        return monHisMapper.throughput(param);
    }

    @Override
    public List<MonHisDO> io(MonHisDO param) {
        return monHisMapper.io(param);
    }

    @Override
    public List<MonHisDO> network(MonHisDO param) {
        return monHisMapper.network(param);
    }

    @Override
    public List<MonHisDO> search(MonHisDO param) {
        return monHisMapper.search(param);
    }

    @Override
    public List<MonHisDO> usageCpu(MonHisDO param) {
        return monHisMapper.usageCpu(param);
    }

    @Override
    public List<MonHisDO> key(MonHisDO param) {
        return monHisMapper.key(param);
    }

    @Override
    public List<MonHisDO> usageRam(MonHisDO param) {
        return monHisMapper.usageRam(param);
    }

    @Override
    public List<MonHisDO> flow(MonHisDO param) {
        return monHisMapper.flow(param);
    }

    @Override
    public List<MonHisDO> analysisUsageCpu(MonHisDO param) {
        return monHisMapper.analysisUsageCpu(param);
    }

    @Override
    public List<MonHisDO> analysisUsageRam(MonHisDO param) {
        return monHisMapper.analysisUsageRam(param);
    }

    @Override
    public List<MonHisDO> analysisLoad(MonHisDO param) {
        return monHisMapper.analysisLoad(param);
    }

    @Override
    public List<MonHisDO> analysisFlow(MonHisDO param) {
        return monHisMapper.analysisFlow(param);
    }

    @Override
    public List<MonHisDO> middlewareDetailList(MonHisDO param) {
        return monHisMapper.middlewareDetailList(param);
    }

}