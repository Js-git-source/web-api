package com.unisguard.webapi.mapper.role;

import com.unisguard.webapi.common.dataobject.role.RoleMenuDO;
import java.util.List;

public interface RoleMenuMapper {
    List<RoleMenuDO> list(RoleMenuDO param);

    void add(RoleMenuDO param);

    RoleMenuDO detail(Long id);

    void edit(RoleMenuDO param);

    void delete(Long id);

    void batch(List<RoleMenuDO> param);

    void deleteByRoleId(Long roleId);
}