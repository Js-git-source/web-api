package com.unisguard.webapi.manage.user.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.user.UserRoleDO;
import com.unisguard.webapi.manage.user.UserRoleManage;
import com.unisguard.webapi.mapper.user.UserRoleMapper;
import java.util.List;
import javax.annotation.Resource;

@Manage
public class UserRoleManageImpl implements UserRoleManage {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRoleDO> list(UserRoleDO param) {
        return userRoleMapper.list(param);
    }

    @Override
    public void add(UserRoleDO param) {
        userRoleMapper.add(param);
    }

    @Override
    public UserRoleDO detail(Long id) {
        return userRoleMapper.detail(id);
    }

    @Override
    public void edit(UserRoleDO param) {
        userRoleMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        userRoleMapper.delete(id);
    }

    @Override
    public List<UserRoleDO> queryListByUserId(Long userId){
       return userRoleMapper.queryListByUserId(userId);
    }

    @Override
    public List<UserRoleDO> queryListByUserIds(List<Long> userIdList){
        return userRoleMapper.queryListByUserIds(userIdList);
    }
}