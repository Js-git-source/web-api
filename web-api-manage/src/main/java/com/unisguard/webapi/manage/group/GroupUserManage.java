package com.unisguard.webapi.manage.group;

import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import java.util.List;

public interface GroupUserManage {
    List<GroupUserDO> list(GroupUserDO param);

    void add(GroupUserDO param);

    GroupUserDO detail(Long id);

    void edit(GroupUserDO param);

    void delete(Long id);

    List<GroupUserDO> queryListByUserId(Long userId);

    List<GroupUserDO> queryListByUserIds(List<Long> userIdList);

    List<GroupUserDO> queryListByGroupIds(List<Long> userIdList);

}