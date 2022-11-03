package com.unisguard.webapi.service.role;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.dataobject.role.RoleDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;

import java.util.List;

public interface RoleService {
    ResponseDO<List<RoleDO>> list(RoleDO param);

    ResponseDO<Long> add(RoleDO param);

    ResponseDO<RoleDO> detail(Long id);

    ResponseDO<Long> edit(RoleDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<RoleDO> editMenu(RoleDO param);

    ResponseDO<List<GroupDO>> detailGroup(RoleDO param);

    ResponseDO<List<RoleDO>> all();

    ResponseDO<Long> updateStatus(RoleDO param);

    ResponseDO<List<MenuDO>> detailMenu(Long id);
}