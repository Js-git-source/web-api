package com.unisguard.webapi.manage.group;

import com.unisguard.webapi.common.dataobject.group.GroupRoleDO;

import java.util.List;

public interface GroupRoleManage {
    List<GroupRoleDO> list(GroupRoleDO param);

    void add(GroupRoleDO param);

    GroupRoleDO detail(Long id);

    void edit(GroupRoleDO param);

    void delete(Long id);

    List<GroupRoleDO> queryListByGroupIds(List<Long> param);

    List<GroupRoleDO> queryListByGroupId(Long groupId);
}