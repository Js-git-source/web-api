package com.unisguard.webapi.service.group.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import com.unisguard.webapi.manage.group.GroupUserManage;
import com.unisguard.webapi.service.group.GroupUserService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class GroupUserServiceImpl implements GroupUserService {
    @Resource
    private GroupUserManage groupUserManage;

    @Override
    public ResponseDO<List<GroupUserDO>> list(GroupUserDO param) {
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<GroupUserDO> groupUserDOList = groupUserManage.list(param);
        return ResponseDO.success(groupUserDOList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(GroupUserDO param) {
        groupUserManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<GroupUserDO> detail(Long id) {
        GroupUserDO groupUserDO = groupUserManage.detail(id);
        return ResponseDO.success(groupUserDO);
    }

    @Override
    public ResponseDO<Long> edit(GroupUserDO param) {
        groupUserManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        groupUserManage.delete(id);
        return ResponseDO.success();
    }
}