package com.unisguard.webapi.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.user.UserRoleDO;
import com.unisguard.webapi.manage.user.UserRoleManage;
import com.unisguard.webapi.service.user.UserRoleService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Resource
    private UserRoleManage userRoleManage;

    @Override
    public ResponseDO<List<UserRoleDO>> list(UserRoleDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<UserRoleDO> userRoleDOList = userRoleManage.list(param);
        return ResponseDO.success(userRoleDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(UserRoleDO param) {
        userRoleManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<UserRoleDO> detail(Long id) {
        UserRoleDO userRoleDO = userRoleManage.detail(id);
        return ResponseDO.success(userRoleDO);
    }

    @Override
    public ResponseDO<Long> edit(UserRoleDO param) {
        userRoleManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        userRoleManage.delete(id);
        return ResponseDO.success();
    }
}