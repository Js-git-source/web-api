package com.unisguard.webapi.manage.mon.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.mon.MonDO;
import com.unisguard.webapi.manage.mon.MonManage;
import com.unisguard.webapi.mapper.mon.MonMapper;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class MonManageImpl implements MonManage {
    @Resource
    private MonMapper monMapper;

    @Override
    public List<MonDO> list(MonDO param) {
        return monMapper.list(param);
    }

    @Override
    public void add(MonDO param) {
        monMapper.add(param);
    }

    @Override
    public MonDO detail(MonDO param) {
        return monMapper.detail(param);
    }

    @Override
    public void edit(MonDO param) {
        monMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        monMapper.delete(id);
    }

    @Override
    public List<MonDO> getEngineList(MonDO param) {
        return monMapper.getEngineList(param);
    }

    @Override
    public List<MonDO> indexList(MonDO param) {
        return monMapper.indexList(param);
    }

    @Override
    public List<MonDO> middlewareList(MonDO param) {
        return monMapper.middlewareList(param);
    }
}