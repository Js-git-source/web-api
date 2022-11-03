package com.unisguard.webapi.mapper.upgrade;

import com.unisguard.webapi.common.dataobject.upgrade.UpgradeDO;

import java.util.List;

public interface UpgradeMapper {
    List<UpgradeDO> list(UpgradeDO param);

    void add(UpgradeDO param);
}