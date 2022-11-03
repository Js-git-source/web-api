package com.unisguard.webapi.controller.api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.service.api.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunsaike
 * @description 系统API管理
 */
@Api(tags = "系统API管理")
@RestController
@RequestMapping(value = "/api")
public class ApiController {
    @Resource
    private ApiService apiService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<ApiDO>> list(ApiDO param) {
        return apiService.list(param);
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    @ApiOperationSupport(order = 20)
    @LogAudit(moduleId = DictConstant.MODULE_API, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = ApiDO.Add.class) ApiDO param) {
        return apiService.add(param);
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/detail")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    public ResponseDO<ApiDO> detail(@Validated(value = ApiDO.ID.class) ApiDO param) {
        return apiService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    @ApiOperationSupport(order = 40)
    @LogAudit(moduleId = DictConstant.MODULE_API, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> edit(@RequestBody @Validated(ApiDO.Edit.class) ApiDO param) {
        return apiService.edit(param);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @LogAudit(moduleId = DictConstant.MODULE_API, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = ApiDO.ID.class) ApiDO param) {
        return apiService.delete(param.getId());
    }
}