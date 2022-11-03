package com.unisguard.webapi.mapper.deploy;

import com.unisguard.webapi.common.dataobject.deploy.DeployDO;
import java.util.List;

public interface DeployMapper {
    List<DeployDO> list(DeployDO param);

    void add(DeployDO param);

    void delete(Long id);

    DeployDO checkExist(DeployDO param);
}