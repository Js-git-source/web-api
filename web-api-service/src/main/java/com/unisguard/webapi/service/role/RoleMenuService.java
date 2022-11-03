package com.unisguard.webapi.service.role;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.role.RoleMenuDO;
import java.util.List;

public interface RoleMenuService {
    ResponseDO<List<RoleMenuDO>> list(RoleMenuDO param);

    ResponseDO<Long> add(RoleMenuDO param);

    ResponseDO<RoleMenuDO> detail(Long id);

    ResponseDO<Long> edit(RoleMenuDO param);

    ResponseDO<Long> delete(Long id);
}