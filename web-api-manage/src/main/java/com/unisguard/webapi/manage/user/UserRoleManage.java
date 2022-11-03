package com.unisguard.webapi.manage.user;

import com.unisguard.webapi.common.dataobject.user.UserRoleDO;
import java.util.List;

public interface UserRoleManage {
    List<UserRoleDO> list(UserRoleDO param);

    void add(UserRoleDO param);

    UserRoleDO detail(Long id);

    void edit(UserRoleDO param);

    void delete(Long id);

    List<UserRoleDO> queryListByUserId(Long userId);

    List<UserRoleDO> queryListByUserIds(List<Long> userIdList);
}