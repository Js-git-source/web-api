package com.unisguard.webapi.controller.dept;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.service.dept.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "部门管理")
@RestController
@RequestMapping(value = "/dept")
public class DeptController {
    @Resource
    private DeptService deptService;

    @ApiOperation(value = "列表")
    @ApiOperationSupport(order = 10)
    @GetMapping(value = "/list")
    public ResponseDO<List<DeptDO>> list(DeptDO param) {
        return deptService.list(param);
    }

    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 20)
    @PostMapping(value = "/add")
    @LogAudit(moduleId = DictConstant.MODULE_DEPT, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = DeptDO.Add.class) DeptDO param) {
        return deptService.add(param);
    }

    @ApiOperation(value = "详情")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    @GetMapping(value = "/detail")
    public ResponseDO<DeptDO> detail(@Validated(value = DeptDO.ID.class) DeptDO param) {
        return deptService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @ApiOperationSupport(order = 40)
    @PutMapping(value = "/edit")
    @LogAudit(moduleId = DictConstant.MODULE_DEPT, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> edit(@RequestBody @Validated(DeptDO.Edit.class) DeptDO param) {
        return deptService.edit(param);
    }

    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @DeleteMapping(value = "/delete")
    @LogAudit(moduleId = DictConstant.MODULE_DEPT, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = DeptDO.ID.class) DeptDO param) {
        return deptService.delete(param.getId());
    }
}