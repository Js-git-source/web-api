package com.unisguard.webapi.manage.user.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.dataobject.user.UserParamDO;
import com.unisguard.webapi.manage.user.UserManage;
import com.unisguard.webapi.mapper.user.UserMapper;
import com.unisguard.webapi.mapper.user.UserRoleMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Manage
public class UserManageImpl implements UserManage {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserDO> list(UserDO param) {
        return userMapper.list(param);
    }

    @Override
    public List<UserDO> queryExcelList(UserDO param) {
        return userMapper.queryExcelList(param);
    }

    @Override
    @Transactional
    public void add(UserDO param) {
        userMapper.add(param);
        batchUserRole(param);
    }

    @Override
    public UserDO detail(Long id) {
        return userMapper.detail(id);
    }

    @Override
    public UserDO info(Long id) {
        return userMapper.info(id);
    }

    @Override
    @Transactional
    public void edit(UserDO param) {
        userMapper.edit(param);
        //添加用户角色 1.先删除原有的角色 2.添加角色
        userRoleMapper.deleteByUserId(param.getId());
        batchUserRole(param);
    }

    @Override
    public void delete(Long id) {
        userMapper.delete(id);
    }

    @Override

    public List<UserDO> queryListByParam(UserParamDO param) {
        return userMapper.queryListByParam(param);
    }

    @Override
    public UserDO login(String account) {
        return userMapper.login(account);
    }

    @Override
    public void updateFailureCount(UserDO userDO) {
        userMapper.updateFailureCount(userDO);
    }

    private void batchUserRole(UserDO param) {
        if (!CollectionUtils.isEmpty(param.getUserRoleList())) {
            param.getUserRoleList().forEach(s -> {
                s.setUserId(param.getId());
                s.setType(DictConstant.ROLE_AUTH);
            });
            userRoleMapper.batch(param.getUserRoleList());
        }
    }

    @Override
    public void deleteBatch(List<Long> param) {
        userMapper.deleteBatch(param);
    }

    @Override
    public UserDO checkUserName(String name) {
        return userMapper.checkUserName(name);
    }

    @Override
    public UserDO checkUserAccount(String account) {
        return userMapper.checkUserAccount(account);
    }

    @Override
    public void updateStatus(UserDO param) {
        userMapper.updateStatus(param);
    }

    @Override
    public void update(UserDO param) {
        userMapper.edit(param);
    }

    @Override
    public String password(Long id) {
        return userMapper.password(id);
    }

    @Override
    public void updateDeptId(Long oldDeptId, Long newDeptId) {
        userMapper.updateDeptId(oldDeptId, newDeptId);
    }

    @Override
    public void resetPassword(UserDO param) {
        userMapper.resetPassword(param);
    }

    @Override
    public void updateFirstLogin(Long id) {
        userMapper.updateFirstLogin(id);
    }

    @Override
    public List<Long> getUserId(String userName) {
        return userMapper.getUserId(userName);
    }
}
