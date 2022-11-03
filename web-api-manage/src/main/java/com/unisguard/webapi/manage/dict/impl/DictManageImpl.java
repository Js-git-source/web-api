package com.unisguard.webapi.manage.dict.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.manage.dict.DictManage;
import com.unisguard.webapi.mapper.dict.DictMapper;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class DictManageImpl implements DictManage {
    @Resource
    private DictMapper dictMapper;

    @Override
    public List<DictDO> list(DictDO param) {
        return dictMapper.list(param);
    }

    @Override
    public void add(DictDO param) {
        dictMapper.add(param);
    }

    @Override
    public DictDO detail(Long id) {
        return dictMapper.detail(id);
    }

    @Override
    public void edit(DictDO param) {
        dictMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        dictMapper.delete(id);
    }

    @Override
    public Integer exists(DictDO dictDO) {
        return dictMapper.exists(dictDO);
    }

    @Override
    public List<DictDO> queryNodePath(DictDO param) {
        return dictMapper.queryNodePath(param);
    }
}