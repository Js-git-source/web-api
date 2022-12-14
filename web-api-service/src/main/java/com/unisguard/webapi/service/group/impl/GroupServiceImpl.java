package com.unisguard.webapi.service.group.impl;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.common.dataobject.group.GroupRoleDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.common.util.GroupUtil;
import com.unisguard.webapi.manage.group.GroupManage;
import com.unisguard.webapi.manage.group.GroupRoleManage;
import com.unisguard.webapi.service.group.GroupService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupManage groupManage;
    @Resource
    private GroupRoleManage groupRoleManage;

    @Override
    public ResponseDO<List<GroupDO>> list(GroupDO param) {
        List<GroupDO> groupList = groupManage.list(param);
        if (CollectionUtils.isEmpty(groupList)) {
            return ResponseDO.success();
        }
        List<GroupRoleDO> groupRoleList = groupRoleManage.queryListByGroupIds(groupList.stream().map(GroupDO::getId).collect(Collectors.toList()));
        Map<Long, List<GroupRoleDO>> groupRoleMap = groupRoleList.stream().collect(Collectors.groupingBy(GroupRoleDO::getGroupId));
        groupList.forEach(s -> s.setGroupRoleList(groupRoleMap.get(s.getId())));
        List<GroupDO> result = GroupUtil.getTree(groupList, -1L);
        return ResponseDO.success(result);
    }

    @Override
    public ResponseDO<Long> add(GroupDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        if (param.getPid() == null || param.getPid() == 0) {
            param.setPid(-1L);
        }
        checkName(param);
        groupManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<GroupDO> detail(Long id) {
        GroupDO groupDO = groupManage.detail(id);
        if (groupDO == null) {
            return ResponseDO.success();
        }
        // ??????????????????????????????
        List<GroupRoleDO> groupRoleList = groupRoleManage.queryListByGroupId(id);
        groupDO.setGroupRoleList(groupRoleList);
        return ResponseDO.success(groupDO);
    }

    @Override
    public ResponseDO<Long> edit(GroupDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        if (param.getPid() == null || param.getPid() == 0) {
            param.setPid(-1L);
        }
        checkName(param);
        groupManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        //????????????????????????????????????????????????????????????????????????
        List<GroupDO> groupDOList = groupManage.queryChildrenList(id);
        if (groupDOList != null && groupDOList.size() > 0) {
            throw new ApplicationException("?????????????????????????????????????????????");
        }
        groupManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<List<GroupDO>> queryList() {
        List<GroupDO> groupList = groupManage.queryList();
        return ResponseDO.success(groupList);
    }

    @Override
    public ResponseDO<Long> updateStatus(GroupDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        groupManage.updateStatus(param);
        return ResponseDO.success(param.getId());
    }

    private void checkName(GroupDO param) {
        GroupDO groupDO = groupManage.checkName(param.getName());
        if (groupDO != null && (param.getId() == null || !groupDO.getId().equals(param.getId()))) {
            throw new ApplicationException("????????????????????????");
        }
    }

}