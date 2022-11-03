package com.unisguard.webapi.manage.common.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.manage.common.CommonManage;
import com.unisguard.webapi.mapper.common.CommonMapper;

import javax.annotation.Resource;

/**
 * 共同方法
 */
@Manage
public class CommonManageImpl implements CommonManage {

    @Resource
    private CommonMapper commonMapper;

    @Override
    public String getUUID() {
        return commonMapper.getUUID();
    }


}
