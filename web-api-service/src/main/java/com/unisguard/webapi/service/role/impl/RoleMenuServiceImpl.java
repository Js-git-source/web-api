package com.unisguard.webapi.service.role.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.role.RoleMenuDO;
import com.unisguard.webapi.manage.role.RoleMenuManage;
import com.unisguard.webapi.service.role.RoleMenuService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Resource
    private RoleMenuManage roleMenuManage;

    @Override
    public ResponseDO<List<RoleMenuDO>> list(RoleMenuDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<RoleMenuDO> roleMenuDOList = roleMenuManage.list(param);
        return ResponseDO.success(roleMenuDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(RoleMenuDO param) {
        roleMenuManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<RoleMenuDO> detail(Long id) {
        RoleMenuDO roleMenuDO = roleMenuManage.detail(id);
        return ResponseDO.success(roleMenuDO);
    }

    @Override
    public ResponseDO<Long> edit(RoleMenuDO param) {
        roleMenuManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        roleMenuManage.delete(id);
        return ResponseDO.success();
    }
}