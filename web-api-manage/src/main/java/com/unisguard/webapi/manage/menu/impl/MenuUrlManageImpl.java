package com.unisguard.webapi.manage.menu.impl;

import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.dataobject.menu.MenuUrlDO;
import com.unisguard.webapi.manage.menu.MenuUrlManage;
import com.unisguard.webapi.mapper.menu.MenuUrlMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Manage
public class MenuUrlManageImpl implements MenuUrlManage {
    @Resource
    private MenuUrlMapper menuUrlMapper;

    @Override
    public List<MenuUrlDO> list(MenuUrlDO param) {
        return menuUrlMapper.list(param);
    }

    @Override
    public void add(List<MenuUrlDO> param) {
        menuUrlMapper.add(param);
    }

    @Override
    public MenuUrlDO detail(Long id) {
        return menuUrlMapper.detail(id);
    }

    @Override
    public void edit(MenuUrlDO param) {
        menuUrlMapper.edit(param);
    }

    @Override
    public void delete(Long id) {
        menuUrlMapper.delete(id);
    }

    @Override
    public List<MenuUrlDO> queryListByMenuId(Long menuId) {
        return menuUrlMapper.queryListByMenuId(menuId);
    }

    @Override
    public Set<String> queryListByUserId(Long userId) {
        return menuUrlMapper.queryListByUserId(userId);
    }
}
