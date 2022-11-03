package com.unisguard.webapi.service.user;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.user.UserRoleDO;
import java.util.List;

public interface UserRoleService {
    ResponseDO<List<UserRoleDO>> list(UserRoleDO param);

    ResponseDO<Long> add(UserRoleDO param);

    ResponseDO<UserRoleDO> detail(Long id);

    ResponseDO<Long> edit(UserRoleDO param);

    ResponseDO<Long> delete(Long id);
}