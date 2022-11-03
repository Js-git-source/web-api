package com.unisguard.webapi.common.util;

import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {
    public static List<MenuDO> getTree(List<MenuDO> menuDOList, Long parentId) {
        List<MenuDO> list = menuDOList.stream().filter(menuDO -> parentId.equals(menuDO.getParentId())).collect(Collectors.toList());
        list.forEach(menuDO -> {
            List<MenuDO> childList = getTree(menuDOList, menuDO.getId()).stream().sorted(Comparator.comparing(MenuDO::getSort)).collect(Collectors.toList());
            menuDO.setChildren(childList);
        });
        return list;
    }

    public static void removeTree(List<MenuDO> menuDOList, List<MenuDO> haveMenuDOList) {
        Iterator<MenuDO> iterator = menuDOList.iterator();
        while (iterator.hasNext()) {
            MenuDO menuDO = iterator.next();
            // 子菜单是否为空
            if (CollectionUtils.isEmpty(menuDO.getChildren())) {
                // 已有菜单包含当前菜单
                if (haveMenuDOList.stream().noneMatch(have -> have.getId().equals(menuDO.getId()))) {
                    iterator.remove();
                }
                continue;
            }
            // 遍历子菜单
            removeTree(menuDO.getChildren(), haveMenuDOList);
            // 遍历完当前菜单，其子菜单为空则删除
            if (CollectionUtils.isEmpty(menuDO.getChildren())) {
                iterator.remove();
            }
        }
    }
}
