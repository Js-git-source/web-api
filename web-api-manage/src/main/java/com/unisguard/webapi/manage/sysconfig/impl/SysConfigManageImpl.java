package com.unisguard.webapi.manage.sysconfig.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import com.unisguard.webapi.manage.sysconfig.SysConfigManage;
import com.unisguard.webapi.mapper.sysconfig.SysConfigMapper;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author sunsaike
 * @description 系统配置
 */
@Manage
public class SysConfigManageImpl implements SysConfigManage {
    @Resource
    private SysConfigMapper sysConfigMapper;

    @Override
    public List<SysConfigDO> list(SysConfigDO param) {
        return sysConfigMapper.list(param);
    }

    @Override
    public void add(SysConfigDO param) {
        sysConfigMapper.add(param);
    }

    @Override
    public SysConfigDO detail(Long id) {
        return sysConfigMapper.detail(id);
    }

    @Override
    public void edit(SysConfigDO param) {
        sysConfigMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        sysConfigMapper.delete(id);
    }

    @Override
    public List<SysConfigDO> queryAll() {
        return sysConfigMapper.queryAll();
    }
}