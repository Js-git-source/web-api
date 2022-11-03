package com.unisguard.webapi.service.auth.impl;

import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.dataobject.login.PasswordDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.exception.MessageCode;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.common.util.DeptUtil;
import com.unisguard.webapi.common.util.SessionUtil;
import com.unisguard.webapi.manage.dept.DeptManage;
import com.unisguard.webapi.manage.role.RoleManage;
import com.unisguard.webapi.manage.user.UserManage;
import com.unisguard.webapi.service.auth.AuthService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    private UserManage userManage;
    @Resource
    private DeptManage deptManage;
    @Resource
    private RoleManage roleManage;

    @Override
    public ResponseDO<List<DictDO>> queryDictList(Integer codeType) {
        List<DictDO> dictList = GlobalConstant.DICT_MAP.get(codeType);
        return ResponseDO.success(dictList);
    }

    @Override
    public void updateUser(UserDO param) {
        CurrentUserDO currentUserDO = CurrentUserUtil.get();
        if (currentUserDO == null) {
            throw new ApplicationException(MessageCode.LOGIN_EXPIRE, "登录过期");
        }
        param.setId(currentUserDO.getId());
        param.setUpdateUserId(currentUserDO.getId());
        param.setUpdateTime(LocalDateTime.now());
        userManage.update(param);
    }

    @Override
    public void updatePassword(PasswordDO param) {
        Long userId = CurrentUserUtil.getId();
        if (userId == null) {
            throw new ApplicationException(MessageCode.LOGIN_EXPIRE, "登录过期");
        }
        String password = userManage.password(userId);
        if (!password.equals(DigestUtils.md5Hex(param.getOldPassword()))) {
            throw new ApplicationException("旧密码错误");
        }
        if (!param.getNewPassword().equals(param.getConfirmPassword())) {
            throw new ApplicationException("新密码与确认密码不一致");
        }
        if (password.equals(DigestUtils.md5Hex(param.getNewPassword()))) {
            throw new ApplicationException("新密码不能与当前密码一致");
        }
        UserDO userDO = new UserDO();
        userDO.setId(userId);
        userDO.setPassword(DigestUtils.md5Hex(param.getNewPassword()));
        userDO.setUpdateTime(LocalDateTime.now());
        userDO.setUpdateUserId(userId);
        userManage.resetPassword(userDO);
        userDO = userManage.detail(userId);
        // 首次登录修改密码，
        if (DictConstant.DEFAULT.equals(userDO.getFailureCount())) {
            userManage.updateFirstLogin(userId);
        }
        SessionUtil.remove(userDO.getAccount());
    }

    @Override
    public ResponseDO<List<DeptDO>> deptTree() {
        List<DeptDO> deptDOList = deptManage.deptTree();
        deptDOList = DeptUtil.getTree(deptDOList, -1L);
        return ResponseDO.success(deptDOList, Long.valueOf(deptDOList.size()));
    }

    @Override
    public ResponseDO<List<DeptDO>> deptList(Long id) {
        List<DeptDO> deptDOList = deptManage.deptList();
        if (null == id) {
            return ResponseDO.success(deptDOList);
        }
        DeptDO detail = deptManage.detail(id);
        if (null == detail) {
            throw new ApplicationException("部门id错误");
        }
        deptDOList = deptDOList.stream()
                .filter(item -> !item.getPath().contains(detail.getPath()))
                .collect(Collectors.toList());
        return ResponseDO.success(deptDOList);
    }

    @Override
    public ResponseDO<CurrentUserDO> queryUser() {
        Long userId = CurrentUserUtil.getId();
        if (userId == null) {
            throw new ApplicationException(MessageCode.LOGIN_EXPIRE, "登录过期");
        }
        UserDO userDO = userManage.detail(userId);
        CurrentUserDO currentUserDO = new CurrentUserDO();
        currentUserDO.setAccount(userDO.getAccount());
        currentUserDO.setName(userDO.getName());
        currentUserDO.setTitle(userDO.getTitle());
        currentUserDO.setPhone(userDO.getPhone());
        currentUserDO.setEmail(userDO.getEmail());
        currentUserDO.setDescription(userDO.getDescription());
        Set<String> roleNameSet = roleManage.queryNameByUserId(userDO.getId());
        currentUserDO.setRoleNameSet(roleNameSet);
        DeptDO deptDO = deptManage.detail(userDO.getDeptId());
        if (deptDO != null) {
            currentUserDO.setDeptName(deptDO.getPath());
        }
        return ResponseDO.success(currentUserDO);
    }
}
