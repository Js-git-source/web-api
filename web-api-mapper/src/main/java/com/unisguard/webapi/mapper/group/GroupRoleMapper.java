package com.unisguard.webapi.mapper.group;

import com.unisguard.webapi.common.dataobject.group.GroupRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupRoleMapper {
    List<GroupRoleDO> list(GroupRoleDO param);

    void add(GroupRoleDO param);

    GroupRoleDO detail(Long id);

    void edit(GroupRoleDO param);

    void delete(Long id);

    void batch(List<GroupRoleDO> param);

    List<GroupRoleDO> queryListByGroupIds(List<Long> param);

    List<GroupRoleDO> queryListByGroupId(Long groupId);

    void deleteBatch(@Param("groupId") Long groupId, @Param("list") List<Long> deleteRoleIdList);
}