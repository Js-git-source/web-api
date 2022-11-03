package com.unisguard.webapi.manage.menu.impl;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.unisguard.webapi.common.annotation.Manage;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.util.MenuUtil;
import com.unisguard.webapi.manage.menu.MenuManage;
import com.unisguard.webapi.mapper.menu.MenuMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Manage
public class MenuManageImpl implements MenuManage {
    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<MenuDO> list() {
        return menuMapper.list();
    }

    @Override
    public void add(MenuDO param) {
        menuMapper.add(param);
    }

    @Override
    public MenuDO detail(Long id) {
        return menuMapper.detail(id);
    }

    @Override
    public void edit(MenuDO param) {
        menuMapper.edit(param);
        // 如果当前菜单状态为隐藏时，查询该菜单下是否有子菜单
        if(DictConstant.DISABLE.equals(param.getStatus())){
            List<MenuDO> menuList = menuMapper.list();
            menuList = MenuUtil.getTree(menuList,param.getId());
            List<MenuDO> modifyMenuList = new ArrayList<>();
            getMenuList(menuList, modifyMenuList);
            if (CollectionUtils.isNotEmpty(modifyMenuList)){
                menuMapper.batchUpdateStatus(modifyMenuList.stream().map(MenuDO::getId).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public void delete(Long id) {
        menuMapper.delete(id);
    }

    @Override
    public List<MenuDO> queryListByRoleId(Long roleId) {
        return menuMapper.queryListByRoleId(roleId);
    }

    @Override
    public List<MenuDO> queryListByRoleIds(List<Long> roleIdList) {
        return menuMapper.queryListByRoleIds(roleIdList);
    }

    @Override
    public List<MenuDO> queryListByUserId(Long userId) {
        return menuMapper.queryListByUserId(userId);
    }

    @Override
    public MenuDO checkTag(String tag) {
        return menuMapper.checkTag(tag);
    }

    @Override
    public MenuDO checkUrl(String url) {
        return menuMapper.checkUrl(url);
    }

    @Override
    public void updateStatus(MenuDO param) {
        menuMapper.updateStatus(param);
    }

    @Override
    public List<MenuDO> queryChildrenList(Long id) {
        return menuMapper.queryChildrenList(id);
    }

    private List<MenuDO> getMenuList(List<MenuDO> param, List<MenuDO> menuDOList){
        param.forEach(menuDO -> {
            menuDOList.add(menuDO);
            if(CollectionUtils.isNotEmpty(menuDO.getChildren())){
                getMenuList(menuDO.getChildren(),menuDOList);
            }
        });
        return menuDOList;
    }
}
