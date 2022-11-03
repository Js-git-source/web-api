package com.unisguard.webapi.manage.config.impl;


import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.lic.LicDO;
import com.unisguard.webapi.manage.config.ConfigManage;
import com.unisguard.webapi.mapper.config.ConfigMapper;

import javax.annotation.Resource;

@Manage
public class ConfigManageImpl implements ConfigManage {

    @Resource
    private ConfigMapper configMapper;

    @Override
    public LicDO findLicense() {
        return configMapper.findLicense();
    }

}
