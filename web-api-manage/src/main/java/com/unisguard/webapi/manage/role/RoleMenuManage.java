package com.unisguard.webapi.manage.role;

import com.unisguard.webapi.common.dataobject.role.RoleMenuDO;
import java.util.List;

public interface RoleMenuManage {
    List<RoleMenuDO> list(RoleMenuDO param);

    void add(RoleMenuDO param);

    RoleMenuDO detail(Long id);

    void edit(RoleMenuDO param);

    void delete(Long id);

}