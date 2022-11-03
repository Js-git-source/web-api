package com.unisguard.webapi.service.group;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import java.util.List;

public interface GroupUserService {
    ResponseDO<List<GroupUserDO>> list(GroupUserDO param);

    ResponseDO<Long> add(GroupUserDO param);

    ResponseDO<GroupUserDO> detail(Long id);

    ResponseDO<Long> edit(GroupUserDO param);

    ResponseDO<Long> delete(Long id);
}