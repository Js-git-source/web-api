package com.unisguard.webapi.mapper.user;

import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.dataobject.user.UserParamDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    List<UserDO> list(UserDO param);

    List<UserDO> queryExcelList(UserDO param);

    void add(UserDO param);

    UserDO detail(Long id);

    UserDO info(Long id);

    void edit(UserDO param);

    void delete(Long id);

    List<UserDO> queryListByParam(UserParamDO param);

    List<UserDO> queryListUserRole(UserDO param);

    List<UserDO> queryListUserGroup(UserDO param);

    UserDO login(String account);

    void updateFailureCount(UserDO userDO);

    void deleteBatch(List<Long> param);

    UserDO checkUserName(String name);

    UserDO checkUserAccount(String account);

    void updateStatus(UserDO param);

    String password(Long id);

    void updateDeptId(@Param("oldDeptId") Long oldDeptId, @Param("newDeptId") Long newDeptId);

    void resetPassword(UserDO param);

    void updateFirstLogin(Long id);

    List<Long> getUserId(String userName);
}
