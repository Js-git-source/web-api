package com.unisguard.webapi.manage.group.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.group.GroupRoleDO;
import com.unisguard.webapi.manage.group.GroupRoleManage;
import com.unisguard.webapi.mapper.group.GroupRoleMapper;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class GroupRoleManageImpl implements GroupRoleManage {
    @Resource
    private GroupRoleMapper groupRoleMapper;

    @Override
    public List<GroupRoleDO> list(GroupRoleDO param) {
        return groupRoleMapper.list(param);
    }

    @Override
    public void add(GroupRoleDO param) {
        groupRoleMapper.add(param);
    }

    @Override
    public GroupRoleDO detail(Long id) {
        return groupRoleMapper.detail(id);
    }

    @Override
    public void edit(GroupRoleDO param) {
        groupRoleMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        groupRoleMapper.delete(id);
    }

    @Override
    public List<GroupRoleDO> queryListByGroupIds(List<Long> param) {
        return groupRoleMapper.queryListByGroupIds(param);
    }

    @Override
    public List<GroupRoleDO> queryListByGroupId(Long groupId) {
        return groupRoleMapper.queryListByGroupId(groupId);
    }
}