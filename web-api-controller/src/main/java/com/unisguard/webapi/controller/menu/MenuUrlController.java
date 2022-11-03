package com.unisguard.webapi.controller.menu;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.menu.MenuUrlDO;
import com.unisguard.webapi.service.menu.MenuUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "菜单接口管理")
@RestController
@RequestMapping(value = "/menu/url")
public class MenuUrlController {
    @Resource
    private MenuUrlService menuUrlService;
    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @ApiOperation(value = "列表")
    @ApiOperationSupport(order = 10)
    @GetMapping(value = "/list")
    public ResponseDO<List<MenuUrlDO>> list(MenuUrlDO param) {
        return menuUrlService.list(param);
    }

    @LogAudit(moduleId = DictConstant.MODULE_MENU, opType = DictConstant.OPT_ADD)
    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 20)
    @PostMapping(value = "/add")
    public ResponseDO<Long> add(@RequestBody List<MenuUrlDO> param) {
        return menuUrlService.add(param);
    }

    @LogAudit(moduleId = DictConstant.MODULE_MENU, opType = DictConstant.OPT_DEL)
    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @DeleteMapping(value = "/delete")
    public ResponseDO<Long> delete(@Validated(value = MenuUrlDO.ID.class) MenuUrlDO param) {
        return menuUrlService.delete(param.getId());
    }

    @ApiOperation(value = "接口列表")
    @ApiOperationSupport(order = 10)
    @GetMapping(value = "/query")
    public ResponseDO<List<MenuUrlDO>> query(MenuUrlDO param, HttpServletRequest request) {
        return menuUrlService.query(param, requestMappingHandlerMapping, request.getServletContext().getRealPath("/"));
    }


}