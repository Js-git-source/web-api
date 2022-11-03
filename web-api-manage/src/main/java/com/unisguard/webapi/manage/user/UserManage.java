package com.unisguard.webapi.manage.user;

import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.dataobject.user.UserParamDO;

import java.util.List;

public interface UserManage {
    List<UserDO> list(UserDO param);

    List<UserDO> queryExcelList(UserDO param);

    void add(UserDO param);

    UserDO detail(Long id);

    void edit(UserDO param);

    void delete(Long id);

    List<UserDO> queryListByParam(UserParamDO param);

    UserDO login(String account);

    void updateFailureCount(UserDO userDO);

    void deleteBatch(List<Long> param);

    UserDO checkUserName(String name);

    UserDO checkUserAccount(String account);

    void updateStatus(UserDO param);

    void update(UserDO param);

    String password(Long id);

    void updateDeptId(Long oldDeptId, Long newDeptId);

    void resetPassword(UserDO param);

    UserDO info(Long id);

    void updateFirstLogin(Long id);

    List<Long> getUserId(String userName);
}
