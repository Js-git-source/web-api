package com.unisguard.webapi.controller.menu;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.service.menu.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping(value = "/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<MenuDO>> list() {
        return menuService.list();
    }

    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 20)
    @PostMapping(value = "/add")
    @LogAudit(moduleId = DictConstant.MODULE_MENU, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = MenuDO.Add.class) MenuDO param) {
        return menuService.add(param);
    }

    @ApiOperation(value = "详情")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    @GetMapping(value = "/detail")
    public ResponseDO<MenuDO> detail(@Validated(value = MenuDO.ID.class) MenuDO param) {
        return menuService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @ApiOperationSupport(order = 40)
    @PutMapping(value = "/edit")
    @LogAudit(moduleId = DictConstant.MODULE_MENU, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> edit(@RequestBody @Validated(MenuDO.Edit.class) MenuDO param) {
        return menuService.edit(param);
    }

    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @DeleteMapping(value = "/delete")
    @LogAudit(moduleId = DictConstant.MODULE_MENU, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = MenuDO.ID.class) MenuDO param) {
        return menuService.delete(param.getId());
    }
}
