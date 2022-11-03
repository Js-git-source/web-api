package com.unisguard.webapi.service.role.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.dataobject.role.RoleDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.common.util.MenuUtil;
import com.unisguard.webapi.manage.group.GroupManage;
import com.unisguard.webapi.manage.group.GroupUserManage;
import com.unisguard.webapi.manage.menu.MenuManage;
import com.unisguard.webapi.manage.role.RoleManage;
import com.unisguard.webapi.service.role.RoleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleManage roleManage;
    @Resource
    private MenuManage menuManage;
    @Resource
    private GroupManage groupManage;
    @Resource
    private GroupUserManage groupUserManage;

    @Override
    public ResponseDO<List<RoleDO>> list(RoleDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<RoleDO> roleDOList = roleManage.list(param);
        return ResponseDO.success(roleDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(RoleDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        checkName(param);
        if (CollectionUtils.isEmpty(param.getMenuList())) {
            throw new ApplicationException("菜单权限不能为空");
        }
        roleManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<RoleDO> detail(Long id) {
        RoleDO roleDO = roleManage.detail(id);
        return ResponseDO.success(roleDO);
    }


    @Override
    public ResponseDO<Long> edit(RoleDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        checkName(param);
        if (CollectionUtils.isEmpty(param.getMenuList())) {
            throw new ApplicationException("菜单权限不能为空");
        }
        roleManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        roleManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<RoleDO> editMenu(RoleDO param) {
        // 查询所有菜单
        List<MenuDO> menuDOList = menuManage.list();
        if (CollectionUtils.isEmpty(menuDOList)) {
            return ResponseDO.success(param);
        }
        param.setMenuList(MenuUtil.getTree(menuDOList, -1L));
        // 获取角色已有的菜单
        List<MenuDO> checkMenuDOList = menuManage.queryListByRoleId(param.getId());
        List<Long> checkIdList = new ArrayList<>();
        getCheckId(menuDOList, checkMenuDOList, 4, checkIdList);
        param.setCheckMenuList(checkIdList);
        return ResponseDO.success(param);
    }

    private void getCheckId(List<MenuDO> menuDOList, List<MenuDO> checkMenuDOList, int i, List<Long> checkIdList) {
        if (i == 0) {
            return;
        }
        checkMenuDOList.stream()
                .filter(item -> i == item.getLevel())
                .forEach(item -> {
                    long checkCount = checkMenuDOList.stream().filter(check -> item.getId().equals(check.getParentId())).count();
                    long allCount = menuDOList.stream().filter(check -> item.getId().equals(check.getParentId())).count();
                    if (allCount == checkCount) {
                        checkIdList.add(item.getId());
                    }
                });
        getCheckId(menuDOList, checkMenuDOList, i - 1, checkIdList);
    }

    @Override
    public ResponseDO<List<MenuDO>> detailMenu(Long id) {
        // 查询所有菜单
        List<MenuDO> menuDOList = menuManage.list();
        // 查询角色已有的菜单
        List<MenuDO> haveMenuDOList = menuManage.queryListByRoleId(id);
        // 菜单转树形结构
        List<MenuDO> treeList = MenuUtil.getTree(menuDOList, -1L);
        // 移除未含有的菜单（递归）
        MenuUtil.removeTree(treeList, haveMenuDOList);
        return ResponseDO.success(treeList);
    }

    @Override
    public ResponseDO<List<GroupDO>> detailGroup(RoleDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        // 查询角色已有的用户组
        List<GroupDO> groupList = groupManage.queryGroupByRoleId(param.getId());
        if (CollectionUtils.isEmpty(groupList)) {
            return ResponseDO.success();
        }
        // 查询用户组已有的用户
        List<GroupUserDO> groupUserList = groupUserManage.queryListByGroupIds(groupList.stream().map(GroupDO::getId).collect(Collectors.toList()));
        // 按groupId分组（map类型）
        Map<Long, List<GroupUserDO>> groupUserMap = groupUserList.stream().collect(Collectors.groupingBy(GroupUserDO::getGroupId));
        groupList.forEach(s -> s.setGroupUserList(groupUserMap.get(s.getId())));
        return ResponseDO.success(groupList, page.getTotal());
    }

    @Override
    public ResponseDO<List<RoleDO>> all() {
        List<RoleDO> roleDOList = roleManage.all();
        return ResponseDO.success(roleDOList);
    }

    @Override
    public ResponseDO<Long> updateStatus(RoleDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        roleManage.updateStatus(param);
        return ResponseDO.success(param.getId());
    }

    private void checkName(RoleDO roleDO) {
        RoleDO checkRole = roleManage.checkName(roleDO.getName());
        if (checkRole != null && (roleDO.getId() == null || !checkRole.getId().equals(roleDO.getId()))) {
            throw new ApplicationException("角色名称已存在");
        }
    }
}
