package com.unisguard.webapi.manage.upgrade.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.upgrade.UpgradeDO;
import com.unisguard.webapi.manage.upgrade.UpgradeManage;
import com.unisguard.webapi.mapper.upgrade.UpgradeMapper;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class UpgradeManageImpl implements UpgradeManage {
    @Resource
    private UpgradeMapper upgradeMapper;

    @Override
    public List<UpgradeDO> list(UpgradeDO param) {
        return upgradeMapper.list(param);
    }

    @Override
    public void add(UpgradeDO param) {
        upgradeMapper.add(param);
    }

}