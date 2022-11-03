package com.unisguard.webapi.manage.menu;

import com.unisguard.webapi.common.dataobject.menu.MenuDO;

import java.util.List;

public interface MenuManage {
    List<MenuDO> list();

    void add(MenuDO param);

    MenuDO detail(Long id);

    void edit(MenuDO param);

    void delete(Long id);

    List<MenuDO> queryListByRoleId(Long roleId);

    List<MenuDO> queryListByRoleIds(List<Long> roleIdList);

    List<MenuDO> queryListByUserId(Long userId);

    MenuDO checkTag(String tag);

    MenuDO checkUrl(String url);

    void updateStatus(MenuDO param);

    List<MenuDO> queryChildrenList(Long id);
}
