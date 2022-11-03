package com.unisguard.webapi.service.auth;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.dataobject.login.PasswordDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;

import java.util.List;

public interface AuthService {
    ResponseDO<List<DictDO>> queryDictList(Integer codeType);

    void updateUser(UserDO param);

    void updatePassword(PasswordDO param);

    ResponseDO<List<DeptDO>> deptTree();

    ResponseDO<List<DeptDO>> deptList(Long id);

    ResponseDO<CurrentUserDO> queryUser();
}
