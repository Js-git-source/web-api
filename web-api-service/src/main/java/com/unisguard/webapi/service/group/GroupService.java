package com.unisguard.webapi.service.group;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.dataobject.user.UserParamDO;

import java.util.List;

public interface GroupService {
    ResponseDO<List<GroupDO>> list(GroupDO param);

    ResponseDO<Long> add(GroupDO param);

    ResponseDO<GroupDO> detail(Long id);

    ResponseDO<Long> edit(GroupDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO<List<GroupDO>> queryList();

    ResponseDO<Long> updateStatus(GroupDO param);
}