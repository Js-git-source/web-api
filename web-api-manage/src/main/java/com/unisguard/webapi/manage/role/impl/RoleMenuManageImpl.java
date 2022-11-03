package com.unisguard.webapi.manage.role.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.role.RoleMenuDO;
import com.unisguard.webapi.manage.role.RoleMenuManage;
import com.unisguard.webapi.mapper.role.RoleMenuMapper;
import java.util.List;
import javax.annotation.Resource;

@Manage
public class RoleMenuManageImpl implements RoleMenuManage {
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<RoleMenuDO> list(RoleMenuDO param) {
        return roleMenuMapper.list(param);
    }

    @Override
    public void add(RoleMenuDO param) {
        roleMenuMapper.add(param);
    }

    @Override
    public RoleMenuDO detail(Long id) {
        return roleMenuMapper.detail(id);
    }

    @Override
    public void edit(RoleMenuDO param) {
        roleMenuMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        roleMenuMapper.delete(id);
    }

}