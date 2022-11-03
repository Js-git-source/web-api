package com.unisguard.webapi.manage.deploy;

import com.unisguard.webapi.common.dataobject.deploy.DeployDO;

import java.util.List;

public interface DeployManage {
    List<DeployDO> list(DeployDO param);

    void add(DeployDO param);

    void delete(Long id);

    DeployDO checkExist(DeployDO param);
}