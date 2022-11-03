package com.unisguard.webapi.service.menu;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;

import java.util.List;

public interface MenuService {
    ResponseDO<List<MenuDO>> list();

    ResponseDO<Long> add(MenuDO param);

    ResponseDO<MenuDO> detail(Long id);

    ResponseDO<Long> edit(MenuDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<Long> updateStatus(MenuDO param);
}
