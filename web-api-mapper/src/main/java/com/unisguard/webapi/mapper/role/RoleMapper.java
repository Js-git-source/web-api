package com.unisguard.webapi.mapper.role;

import com.unisguard.webapi.common.dataobject.role.RoleDO;

import java.util.List;
import java.util.Set;

public interface RoleMapper {
    List<RoleDO> list(RoleDO param);

    void add(RoleDO param);

    RoleDO detail(Long id);

    void edit(RoleDO param);

    void delete(Long id);

    List<RoleDO> all();

    RoleDO checkName(String name);

    void updateStatus(RoleDO param);

    Set<String> queryNameByUserId(Long userId);
}
