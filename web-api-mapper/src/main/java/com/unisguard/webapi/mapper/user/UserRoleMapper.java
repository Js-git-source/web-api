package com.unisguard.webapi.mapper.user;

import com.unisguard.webapi.common.dataobject.user.UserRoleDO;
import java.util.List;

public interface UserRoleMapper {
    List<UserRoleDO> list(UserRoleDO param);

    void add(UserRoleDO param);

    UserRoleDO detail(Long id);

    void edit(UserRoleDO param);

    void delete(Long id);

    void batch(List<UserRoleDO> param);

    List<UserRoleDO> queryListByUserId(Long userId);

    List<UserRoleDO> queryListByUserIds(List<Long> userIdList);

    void deleteByUserId(Long userId);
}