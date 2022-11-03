package com.unisguard.webapi.service.user;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.dataobject.user.UserParamDO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    ResponseDO<List<UserDO>> list(UserDO param);

    ResponseDO<Long> add(UserDO param);

    ResponseDO<UserDO> detail(Long id);

    ResponseDO<Long> edit(UserDO param);

    ResponseDO<Long> delete(Long id);

    ResponseDO download(HttpServletResponse response, UserDO param);

    ResponseDO<List<UserDO>> queryListByParam(UserParamDO param);

    ResponseDO<List<UserDO>> queryListByParamNoPage(UserParamDO param);

    ResponseDO deleteBatch(List<Long> param);

    ResponseDO<Long> updateStatus(UserDO param);

    ResponseDO<Long> resetPassword(UserDO param);

    ResponseDO<String> generatePassword();

    ResponseDO<List<DeptDO>> deptTree();
}