package com.unisguard.webapi.manage.weakpass.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.weakpass.WeakPassDO;
import com.unisguard.webapi.manage.weakpass.WeakPassManage;
import com.unisguard.webapi.mapper.weakpass.WeakPassMapper;
import java.util.List;
import javax.annotation.Resource;

@Manage
public class WeakPassManageImpl implements WeakPassManage {
    @Resource
    private WeakPassMapper weakPassMapper;

    @Override
    public List<WeakPassDO> list(WeakPassDO param) {
        return weakPassMapper.list(param);
    }

    @Override
    public void add(WeakPassDO param) {
        weakPassMapper.add(param);
    }

    @Override
    public WeakPassDO detail(Long id) {
        return weakPassMapper.detail(id);
    }

    @Override
    public void edit(WeakPassDO param) {
        weakPassMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        weakPassMapper.delete(id);
    }
}