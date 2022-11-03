package com.unisguard.webapi.service.menu.impl;

import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.MenuUtil;
import com.unisguard.webapi.manage.menu.MenuManage;
import com.unisguard.webapi.service.menu.MenuService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuManage menuManage;

    @Override
    public ResponseDO<List<MenuDO>> list() {
        List<MenuDO> menuDOList = menuManage.list();
        //转换树形结构
        menuDOList = MenuUtil.getTree(menuDOList, -1L);
        menuDOList.sort(Comparator.comparing(MenuDO::getSort));
        return ResponseDO.success(menuDOList);
    }

    @Override
    public ResponseDO<Long> add(MenuDO param) {
        validateMenu(param);
        menuManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<MenuDO> detail(Long id) {
        MenuDO menuDO = menuManage.detail(id);
        return ResponseDO.success(menuDO);
    }

    @Override
    public ResponseDO<Long> edit(MenuDO param) {
        validateMenu(param);
        menuManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        //判断该菜单下是否有子菜单
        List<MenuDO> menuDOList = menuManage.queryChildrenList(id);
        if (CollectionUtils.isNotEmpty(menuDOList)) {
            throw new ApplicationException("该菜单下存在未被删除的菜单");
        }
        menuManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> updateStatus(MenuDO param) {
        menuManage.updateStatus(param);
        return ResponseDO.success();
    }

    private void validateMenu(MenuDO param) {
        // 如果菜单类型是菜单的话 校验新窗口和请求地址字段为必输
        if (DictConstant.MENU.equals(param.getType())) {
            if (param.getOpenWin() == null) {
                throw new ApplicationException("新窗口不能为空");
            }
            if (StringUtils.isBlank(param.getUrl())) {
                throw new ApplicationException("请求地址不能为空");
            }
        }
        // 判断如果是根目录的话把parentId设置为-1 level设置为1
        if (param.getParentId() == null || param.getParentId() == 0) {
            param.setParentId(-1L);
            param.setLevel(1);
        }

        MenuDO menuDO = menuManage.detail(param.getParentId());
        if ((menuDO == null || menuDO.getLevel() == null || menuDO.getLevel() == 1) && !param.getType().equals(DictConstant.CATALOG)) {
            // 如果父级菜单是1级目录或者无父级菜单只能创建目录
            throw new ApplicationException("根菜单或一级菜单下添加的菜单类型只能是目录");
        } else if (menuDO != null && menuDO.getLevel() == 2 && !param.getType().equals(DictConstant.MENU)) {
            // 如果父级菜单是2级目录只能创建菜单
            throw new ApplicationException("二级目录下添加的菜单类型只能是菜单");
        } else if (menuDO != null && menuDO.getLevel() == 3 && !param.getType().equals(DictConstant.BUTTON)) {
            // 如果父级菜单是3级目录只能创建按钮
            throw new ApplicationException("三级菜单下添加的菜单类型只能是按钮");
        }
        // 校验菜单标识和请求地址的唯一性
        MenuDO checkTag = menuManage.checkTag(param.getTag());
        if (checkTag != null && (param.getId() == null || !checkTag.getId().equals(param.getId()))) {
            throw new ApplicationException("菜单标识已存在");
        }
        if (StringUtils.isNotBlank(param.getUrl())) {
            MenuDO checkUrl = menuManage.checkUrl(param.getUrl());
            if (checkUrl != null && (param.getId() == null || !checkUrl.getId().equals(param.getId()))) {
                throw new ApplicationException("请求地址已存在");
            }
        }
        if (menuDO == null) {
            param.setLevel(1);
        } else {
            param.setLevel(menuDO.getLevel() + 1);
        }
    }
}
