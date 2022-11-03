package com.unisguard.webapi.service.group.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupRoleDO;
import com.unisguard.webapi.manage.group.GroupRoleManage;
import com.unisguard.webapi.service.group.GroupRoleService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class GroupRoleServiceImpl implements GroupRoleService {
    @Resource
    private GroupRoleManage groupRoleManage;

    @Override
    public ResponseDO<List<GroupRoleDO>> list(GroupRoleDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<GroupRoleDO> groupRoleDOList = groupRoleManage.list(param);
        return ResponseDO.success(groupRoleDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(GroupRoleDO param) {
        groupRoleManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<GroupRoleDO> detail(Long id) {
        GroupRoleDO groupRoleDO = groupRoleManage.detail(id);
        return ResponseDO.success(groupRoleDO);
    }

    @Override
    public ResponseDO<Long> edit(GroupRoleDO param) {
        groupRoleManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        groupRoleManage.delete(id);
        return ResponseDO.success();
    }
}