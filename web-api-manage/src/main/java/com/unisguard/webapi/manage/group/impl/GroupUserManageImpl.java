package com.unisguard.webapi.manage.group.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import com.unisguard.webapi.manage.group.GroupUserManage;
import com.unisguard.webapi.mapper.group.GroupUserMapper;
import java.util.List;
import javax.annotation.Resource;

@Manage
public class GroupUserManageImpl implements GroupUserManage {
    @Resource
    private GroupUserMapper groupUserMapper;

    @Override
    public List<GroupUserDO> list(GroupUserDO param) {
        return groupUserMapper.list(param);
    }

    @Override
    public void add(GroupUserDO param) {
        groupUserMapper.add(param);
    }

    @Override
    public GroupUserDO detail(Long id) {
        return groupUserMapper.detail(id);
    }

    @Override
    public void edit(GroupUserDO param) {
        groupUserMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        groupUserMapper.delete(id);
    }

    @Override
    public List<GroupUserDO> queryListByUserId(Long userId){
        return groupUserMapper.queryListByUserId(userId);
    }

    @Override
    public List<GroupUserDO> queryListByUserIds(List<Long> userIdList){
        return groupUserMapper.queryListByUserIds(userIdList);
    }

    @Override
    public List<GroupUserDO> queryListByGroupIds(List<Long> userIdList){
        return groupUserMapper.queryListByGroupIds(userIdList);
    }
}