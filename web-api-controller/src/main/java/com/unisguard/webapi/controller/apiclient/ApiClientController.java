package com.unisguard.webapi.controller.apiclient;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiAuthDO;
import com.unisguard.webapi.common.dataobject.apiclient.ApiClientDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.service.apiclient.ApiClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @description 系统API客户端管理
 * @author sunsaike
 */
@Api(tags = "API客户端管理")
@RestController
@RequestMapping(value = "/api/client")
public class ApiClientController {
    @Resource
    private ApiClientService apiClientService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<ApiClientDO>> list(ApiClientDO param) {
        return apiClientService.list(param);
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    @ApiOperationSupport(order = 20)
    @LogAudit(moduleId = DictConstant.MODULE_API_CLIENT, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = ApiClientDO.Add.class) ApiClientDO param) {
        return apiClientService.add(param);
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/detail")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    public ResponseDO<ApiClientDO> detail(@Validated(value = ApiClientDO.ID.class) ApiClientDO param) {
        return apiClientService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    @ApiOperationSupport(order = 40)
    @LogAudit(moduleId = DictConstant.MODULE_API_CLIENT, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> edit(@RequestBody @Validated(ApiClientDO.Edit.class) ApiClientDO param) {
        return apiClientService.edit(param);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @LogAudit(moduleId = DictConstant.MODULE_API_CLIENT, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = ApiClientDO.ID.class) ApiClientDO param) {
        return apiClientService.delete(param.getId());
    }

    @ApiOperation(value = "修改状态")
    @PutMapping(value = "/editStatus")
    @ApiOperationSupport(order = 60, includeParameters = {"param.id", "param.status"})
    public ResponseDO<Boolean> editStatus(@RequestBody ApiClientDO param) {
        return apiClientService.editStatus(param);
    }

    @ApiOperation(value = "已授权列表")
    @PostMapping(value = "/apiList")
    @ApiOperationSupport(order = 70, includeParameters = {"param.clientId"})
    public ResponseDO<List<ApiDO>> apiList(@RequestBody ApiAuthDO param) {
        return apiClientService.apiList(param);
    }

    @ApiOperation(value = "当前客户端未授权列表")
    @PostMapping(value = "/notApiList")
    @ApiOperationSupport(order = 80, includeParameters = {"param.clientId"})
    public ResponseDO<List<ApiDO>> notApiList(@RequestBody ApiAuthDO param) {
        return apiClientService.notApiList(param);
    }

    @ApiOperation(value = "授权")
    @PostMapping(value = "/apiAuth")
    @ApiOperationSupport(order = 90)
    @LogAudit(moduleId = DictConstant.MODULE_API_CLIENT, opType = DictConstant.OPT_GRANT)
    public ResponseDO<Boolean> apiAuth(@RequestBody List<ApiAuthDO> param) {
        return apiClientService.apiAuth(param);
    }

    @ApiOperation(value = "取消授权")
    @PostMapping(value = "/deleteAuth")
    @ApiOperationSupport(order = 100)
    public ResponseDO<Boolean> cancelAuth(@RequestBody List<Long> param) {
        return apiClientService.cancelAuth(param);
    }

    @ApiOperation(value = "生成APPKEY")
    @GetMapping(value = "/createAppkey")
    @ApiOperationSupport(order = 110)
    public ResponseDO<String> createAppkey() {
        return apiClientService.createAppkey();
    }
}