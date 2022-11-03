package com.unisguard.webapi.controller.login;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.dataobject.base.BaseDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.dataobject.login.PasswordDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.service.auth.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jiangshan
 * @date 2022/1/13 16:37
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Resource
    private AuthService authService;

    @ApiOperation(value = "字典查询")
    @ApiOperationSupport(order = 10)
    @GetMapping("/dict")
    public ResponseDO<List<DictDO>> queryDictList(Integer codeType) {
        return authService.queryDictList(codeType);
    }

    @ApiOperation(value = "部门树列表")
    @ApiOperationSupport(order = 20)
    @GetMapping(value = "/dept/tree")
    public ResponseDO<List<DeptDO>> deptTree() {
        return authService.deptTree();
    }

    @ApiOperation(value = "部门下拉列表")
    @ApiOperationSupport(order = 30)
    @GetMapping(value = "/dept/list")
    public ResponseDO<List<DeptDO>> deptList(Long id) {
        return authService.deptList(id);
    }

    @ApiOperation(value = "修改用户")
    @ApiOperationSupport(order = 40)
    @PutMapping("/update/user")
    public ResponseDO updateUser(@RequestBody UserDO param) {
        authService.updateUser(param);
        return ResponseDO.success();
    }

    @ApiOperation(value = "修改密码")
    @ApiOperationSupport(order = 50)
    @PutMapping("/update/password")
    public ResponseDO updatePassword(@Validated(BaseDO.Edit.class) @RequestBody PasswordDO param) {
        authService.updatePassword(param);
        return ResponseDO.success();
    }

    @ApiOperation(value = "查询用户")
    @ApiOperationSupport(order = 60)
    @GetMapping("/query/user")
    public ResponseDO<CurrentUserDO> queryUser() {
        return authService.queryUser();
    }
}
