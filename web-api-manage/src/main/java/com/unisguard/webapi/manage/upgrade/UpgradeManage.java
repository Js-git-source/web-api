package com.unisguard.webapi.manage.upgrade;

import com.unisguard.webapi.common.dataobject.upgrade.UpgradeDO;
import java.util.List;

public interface UpgradeManage {
    List<UpgradeDO> list(UpgradeDO param);

    void add(UpgradeDO param);

}