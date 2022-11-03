package com.unisguard.webapi.service.group;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupRoleDO;
import java.util.List;

public interface GroupRoleService {
    ResponseDO<List<GroupRoleDO>> list(GroupRoleDO param);

    ResponseDO<Long> add(GroupRoleDO param);

    ResponseDO<GroupRoleDO> detail(Long id);

    ResponseDO<Long> edit(GroupRoleDO param);

    ResponseDO<Long> delete(Long id);
}