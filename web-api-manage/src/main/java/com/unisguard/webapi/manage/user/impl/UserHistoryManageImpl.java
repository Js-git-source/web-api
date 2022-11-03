package com.unisguard.webapi.manage.user.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.user.UserHistoryDO;
import com.unisguard.webapi.manage.user.UserHistoryManage;
import com.unisguard.webapi.mapper.user.UserHistoryMapper;
import java.util.List;
import javax.annotation.Resource;

@Manage
public class UserHistoryManageImpl implements UserHistoryManage {
    @Resource
    private UserHistoryMapper userHistoryMapper;

    @Override
    public List<UserHistoryDO> list(UserHistoryDO param) {
        return userHistoryMapper.list(param);
    }

    @Override
    public void add(UserHistoryDO param) {
        userHistoryMapper.add(param);
    }

    @Override
    public UserHistoryDO detail(Long id) {
        return userHistoryMapper.detail(id);
    }

    @Override
    public void edit(UserHistoryDO param) {
        userHistoryMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        userHistoryMapper.delete(id);
    }
}