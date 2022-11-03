package com.unisguard.webapi.mapper.user;

import com.unisguard.webapi.common.dataobject.user.UserHistoryDO;
import java.util.List;

public interface UserHistoryMapper {
    List<UserHistoryDO> list(UserHistoryDO param);

    void add(UserHistoryDO param);

    UserHistoryDO detail(Long id);

    void edit(UserHistoryDO param);

    void delete(Long id);
}