package com.unisguard.webapi.manage.role.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.role.RoleDO;
import com.unisguard.webapi.common.dataobject.role.RoleMenuDO;
import com.unisguard.webapi.manage.role.RoleManage;
import com.unisguard.webapi.mapper.role.RoleMapper;
import com.unisguard.webapi.mapper.role.RoleMenuMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Manage
public class RoleManageImpl implements RoleManage {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<RoleDO> list(RoleDO param) {
        return roleMapper.list(param);
    }

    @Override
    @Transactional
    public void add(RoleDO param) {
        roleMapper.add(param);
        batchRoleMenu(param);
    }

    @Override
    public RoleDO detail(Long id) {
        return roleMapper.detail(id);
    }

    @Override
    @Transactional
    public void edit(RoleDO param) {
        roleMapper.edit(param);
        roleMenuMapper.deleteByRoleId(param.getId());
        batchRoleMenu(param);
    }

    @Override
    public void delete(Long id) {
        roleMapper.delete(id);
    }

    @Override
    public List<RoleDO> all() {
        return roleMapper.all();
    }

    @Override
    public RoleDO checkName(String name) {
        return roleMapper.checkName(name);
    }

    @Override
    public void updateStatus(RoleDO param) {
        roleMapper.updateStatus(param);
    }

    private void batchRoleMenu(RoleDO param) {
        //关联角色菜单关系
        if (!CollectionUtils.isEmpty(param.getMenuList())) {
            List<RoleMenuDO> roleMenuDOList = new ArrayList<>();
            param.getMenuList().stream().forEach(s -> {
                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setRoleId(param.getId());
                roleMenuDO.setMenuId(s.getId());
                roleMenuDOList.add(roleMenuDO);
            });
            roleMenuMapper.batch(roleMenuDOList);
        }
    }

    @Override
    public Set<String> queryNameByUserId(Long userId) {
        return roleMapper.queryNameByUserId(userId);
    }
}
