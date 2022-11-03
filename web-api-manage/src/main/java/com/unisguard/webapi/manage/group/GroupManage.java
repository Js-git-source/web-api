package com.unisguard.webapi.manage.group;

import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.common.dataobject.role.RoleDO;

import java.util.List;

public interface GroupManage {
    List<GroupDO> list(GroupDO param);

    void add(GroupDO param);

    GroupDO detail(Long id);

    void edit(GroupDO param);

    void delete(Long id);

    List<GroupDO> queryGroupByRoleId(Long roleId);

    List<GroupDO> queryList();

    GroupDO checkName(String name);

    void updateStatus(GroupDO param);

    List<GroupDO> queryChildrenList(Long id);
}