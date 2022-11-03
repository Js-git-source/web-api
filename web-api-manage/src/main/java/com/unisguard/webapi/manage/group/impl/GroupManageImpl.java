package com.unisguard.webapi.manage.group.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.common.dataobject.group.GroupRoleDO;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import com.unisguard.webapi.manage.group.GroupManage;
import com.unisguard.webapi.mapper.group.GroupMapper;
import com.unisguard.webapi.mapper.group.GroupRoleMapper;
import com.unisguard.webapi.mapper.group.GroupUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Manage
public class GroupManageImpl implements GroupManage {
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupRoleMapper groupRoleMapper;
    @Resource
    private GroupUserMapper groupUserMapper;

    @Override
    public List<GroupDO> list(GroupDO param) {
        return groupMapper.list(param);
    }

    @Override
    @Transactional
    public void add(GroupDO param) {
        groupMapper.add(param);
        //保存用户组角色 用户组用户之间的对应关系
        if (param.getGroupRoleList() != null && param.getGroupRoleList().size() > 0) {
            param.getGroupRoleList().forEach(s -> s.setGroupId(param.getId()));
            groupRoleMapper.batch(param.getGroupRoleList());
        }
        if (param.getGroupUserList() != null && param.getGroupUserList().size() > 0) {
            param.getGroupUserList().forEach(s -> s.setGroupId(param.getId()));
            groupUserMapper.batch(param.getGroupUserList());
        }
    }

    @Override
    public GroupDO detail(Long id) {
        return groupMapper.detail(id);
    }

    @Override
    @Transactional
    public void edit(GroupDO param) {
        groupMapper.edit(param);
        // 处理用户组-角色
        handleGroupRole(param);
        // 处理用户组-用户
        handleGroupUser(param);
    }

    /**
     * 处理用户组-用户
     *
     * @param param
     */
    private void handleGroupUser(GroupDO param) {
        List<GroupUserDO> groupUserList = groupUserMapper.queryListByGroupId(param.getId());
        // 需要删除用户组-用户关系
        List<Long> deleteUserList = groupUserList.stream()
                .filter(oldGroupUser -> param.getGroupUserList().stream().noneMatch(newGroupUser -> oldGroupUser.getUserId().equals(newGroupUser.getUserId())))
                .map(GroupUserDO::getUserId).collect(Collectors.toList());
        if (!deleteUserList.isEmpty()) {
            groupUserMapper.deleteBatch(param.getId(), deleteUserList);
        }
        // 需要保存用户组-用户关系
        if (CollectionUtils.isEmpty(param.getGroupUserList())) {
            return;
        }
        List<GroupUserDO> savGroupUserList = param.getGroupUserList().stream()
                .filter(newGroupUser -> groupUserList.stream().noneMatch(oldGroupUser -> newGroupUser.getUserId().equals(oldGroupUser.getUserId())))
                .peek(newGroupUser -> newGroupUser.setGroupId(param.getId()))
                .collect(Collectors.toList());
        if (!savGroupUserList.isEmpty()) {
            groupUserMapper.batch(savGroupUserList);
        }
    }

    /**
     * 处理用户组-角色s
     *
     * @param param
     */
    private void handleGroupRole(GroupDO param) {
        // 先获取用户组-角色、用户组-用户之前的关联关系
        List<GroupRoleDO> groupRoleList = groupRoleMapper.queryListByGroupId(param.getId());
        // 需要删除的用户组-角色关系
        List<Long> deleteRoleList = groupRoleList.stream()
                .filter(oldGroupRole -> param.getGroupRoleList().stream().noneMatch(newGroupRole -> oldGroupRole.getRoleId().equals(newGroupRole.getRoleId())))
                .map(GroupRoleDO::getRoleId).collect(Collectors.toList());
        if (!deleteRoleList.isEmpty()) {
            groupRoleMapper.deleteBatch(param.getId(), deleteRoleList);
        }
        if (CollectionUtils.isEmpty(param.getGroupRoleList())) {
            return;
        }
        // 需要保存的用户组-角色关系
        List<GroupRoleDO> saveRoleList = param.getGroupRoleList().stream()
                .filter(newGroupRole -> groupRoleList.stream().noneMatch(oldGroupRole -> newGroupRole.getRoleId().equals(oldGroupRole.getRoleId())))
                .peek(newGroupRole -> newGroupRole.setGroupId(param.getId()))
                .collect(Collectors.toList());
        if (!saveRoleList.isEmpty()) {
            groupRoleMapper.batch(saveRoleList);
        }
    }

    @Override
    public void delete(Long id) {
        groupMapper.delete(id);
    }

    @Override
    public List<GroupDO> queryGroupByRoleId(Long roleId) {
        return groupMapper.queryGroupByRoleId(roleId);
    }

    @Override
    public List<GroupDO> queryList() {
        return groupMapper.queryList();
    }

    @Override
    public GroupDO checkName(String name) {
        return groupMapper.checkName(name);
    }

    @Override
    public void updateStatus(GroupDO param) {
        groupMapper.updateStatus(param);
    }

    @Override
    public List<GroupDO> queryChildrenList(Long id) {
        return groupMapper.queryChildrenList(id);
    }
}