package com.unisguard.webapi.service.deploy;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.deploy.DeployDO;
import java.util.List;

public interface DeployService {
    ResponseDO<List<DeployDO>> list(DeployDO param);

    ResponseDO<Long> add(DeployDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<List<DeployDO>> monList(DeployDO param);
}