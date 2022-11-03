package com.unisguard.webapi.mapper.group;

import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupUserMapper {
    List<GroupUserDO> list(GroupUserDO param);

    void add(GroupUserDO param);

    GroupUserDO detail(Long id);

    void edit(GroupUserDO param);

    void delete(Long id);

    void batch(List<GroupUserDO> param);

    List<GroupUserDO> queryListByUserId(Long userId);

    List<GroupUserDO> queryListByUserIds(List<Long> userIdList);

    List<GroupUserDO> queryListByGroupIds(List<Long> groupIdList);

    List<GroupUserDO> queryListByGroupId(Long groupId);

    void deleteBatch(@Param("groupId") Long groupId, @Param("list") List<Long> deleteUserList);
}