package com.unisguard.webapi.controller.deploy;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.deploy.DeployDO;
import com.unisguard.webapi.service.deploy.DeployService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统部署")
@RestController
@RequestMapping(value = "/deploy")
public class DeployController {
    @Resource
    private DeployService deployService;

    @ApiOperation(value = "列表")
    @ApiOperationSupport(order = 10)
    @GetMapping(value = "/list")
    public ResponseDO<List<DeployDO>> list(DeployDO param) {
        return deployService.list(param);
    }

    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 20)
    @PostMapping(value = "/add")
    public ResponseDO<Long> add(@RequestBody @Validated(value = DeployDO.Add.class) DeployDO param) {
        return deployService.add(param);
    }

    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    @DeleteMapping(value = "/delete")
    public ResponseDO<Long> delete(@Validated(value = DeployDO.ID.class) DeployDO param) {
        return deployService.delete(param.getId());
    }

    @ApiOperation(value = "消息监控列表")
    @ApiOperationSupport(order = 40)
    @GetMapping(value = "/mon/list")
    public ResponseDO<List<DeployDO>> monList(DeployDO param) {
        return deployService.monList(param);
    }

}