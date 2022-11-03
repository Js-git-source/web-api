package com.unisguard.webapi.controller.user;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.dataobject.user.UserParamDO;
import com.unisguard.webapi.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    private UserService userService;

    @ApiOperation(value = "列表")
    @ApiOperationSupport(order = 10)
    @GetMapping(value = "/list")
    public ResponseDO<List<UserDO>> list(UserDO param) {
        return userService.list(param);
    }

    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 20)
    @PostMapping(value = "/add")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = UserDO.Add.class) UserDO param) {
        return userService.add(param);
    }

    @ApiOperation(value = "详情")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    @GetMapping(value = "/detail")
    public ResponseDO<UserDO> detail(@Validated(value = UserDO.ID.class) UserDO param) {
        return userService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @ApiOperationSupport(order = 40)
    @PutMapping(value = "/edit")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> edit(@RequestBody @Validated(UserDO.Edit.class) UserDO param) {
        return userService.edit(param);
    }

    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @DeleteMapping(value = "/delete")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = UserDO.ID.class) UserDO param) {
        return userService.delete(param.getId());
    }

    @ApiOperation(value = "导入")
    @ApiOperationSupport(order = 60)
    @GetMapping(value = "/upload")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_DOWNLOAD)
    public ResponseDO<UserDO> upload(UserDO param) {
        return userService.detail(param.getId());
    }

    @ApiOperation(value = "导出")
    @ApiOperationSupport(order = 70)
    @GetMapping(value = "/download")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_DOWNLOAD)
    public ResponseDO download(HttpServletResponse response, UserDO param) {
        return userService.download(response, param);
    }

    @ApiOperation(value = "分页查询关联用户")
    @ApiOperationSupport(order = 80)
    @GetMapping(value = "/list/page")
    public ResponseDO<List<UserDO>> queryListByParam(UserParamDO param) {
        return userService.queryListByParam(param);
    }


    @ApiOperation(value = "批量删除")
    @ApiOperationSupport(order = 90)
    @DeleteMapping(value = "/batch/delete")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_DEL)
    public ResponseDO deleteBatch(@RequestBody List<Long> idList) {
        return userService.deleteBatch(idList);
    }

    @ApiOperation(value = "状态更新")
    @ApiOperationSupport(order = 100)
    @PutMapping(value = "/status/update")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> updateStatus(@RequestBody UserDO param) {
        return userService.updateStatus(param);
    }

    @ApiOperation(value = "重置密码")
    @ApiOperationSupport(order = 100)
    @PutMapping(value = "/password/reset")
    @LogAudit(moduleId = DictConstant.MODULE_USER, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> resetPassword(@RequestBody UserDO param) {
        return userService.resetPassword(param);
    }

    @ApiOperation(value = "生成密码")
    @ApiOperationSupport(order = 110)
    @GetMapping(value = "/password/generate")
    public ResponseDO<String> generatePassword() {
        return userService.generatePassword();
    }

    @ApiOperation(value = "部门树列表")
    @GetMapping(value = "/dept/tree")
    @ApiOperationSupport(order = 120)
    public ResponseDO<List<DeptDO>> deptTree() {
        return userService.deptTree();
    }

    @ApiOperation(value = "不分页查询关联用户")
    @GetMapping(value = "/list/page/no")
    @ApiOperationSupport(order = 130)
    public ResponseDO<List<UserDO>> queryListByParamNoPage(UserParamDO param) {
        return userService.queryListByParamNoPage(param);
    }

}