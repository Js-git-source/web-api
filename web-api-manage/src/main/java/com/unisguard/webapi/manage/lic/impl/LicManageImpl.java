package com.unisguard.webapi.manage.lic.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.lic.LicDO;
import com.unisguard.webapi.manage.lic.LicManage;
import com.unisguard.webapi.mapper.lic.LicMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.annotation.Resource;

@Manage
public class LicManageImpl implements LicManage {
    @Resource
    private LicMapper licMapper;

    @Override
    public List<LicDO> list(LicDO param) {
        return licMapper.list(param);
    }

    @Override
    @Transactional
    public void add(LicDO param) {
        licMapper.updateStatus();
        licMapper.add(param);
    }

    @Override
    public LicDO last() {
        return licMapper.last();
    }

    @Override
    public void edit(LicDO param) {
        licMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        licMapper.delete(id);
    }
}