package com.unisguard.webapi.mapper.menu;

import com.unisguard.webapi.common.dataobject.menu.MenuUrlDO;

import java.util.List;
import java.util.Set;

public interface MenuUrlMapper {
    List<MenuUrlDO> list(MenuUrlDO param);

    void add(List<MenuUrlDO> param);

    MenuUrlDO detail(Long id);

    void edit(MenuUrlDO param);

    void delete(Long id);

    List<MenuUrlDO> queryListByMenuId(Long menuId);

    Set<String> queryListByUserId(Long userId);
}
