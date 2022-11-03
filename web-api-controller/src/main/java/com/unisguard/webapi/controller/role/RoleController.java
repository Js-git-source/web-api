package com.unisguard.webapi.controller.role;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.dataobject.role.RoleDO;
import com.unisguard.webapi.service.role.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "角色管理")
@RestController
@RequestMapping(value = "/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<RoleDO>> list(RoleDO param) {
        return roleService.list(param);
    }

    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 20)
    @PostMapping(value = "/add")
    @LogAudit(moduleId = DictConstant.MODULE_ROLE, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = RoleDO.Add.class) RoleDO param) {
        return roleService.add(param);
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/detail")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    public ResponseDO<RoleDO> detail(@Validated(value = RoleDO.ID.class) RoleDO param) {
        return roleService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @ApiOperationSupport(order = 40)
    @PutMapping(value = "/edit")
    @LogAudit(moduleId = DictConstant.MODULE_ROLE, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> edit(@RequestBody @Validated(RoleDO.Edit.class) RoleDO param) {
        return roleService.edit(param);
    }

    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @DeleteMapping(value = "/delete")
    @LogAudit(moduleId = DictConstant.MODULE_ROLE, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = RoleDO.ID.class) RoleDO param) {
        return roleService.delete(param.getId());
    }

    @ApiOperation(value = "状态更新")
    @ApiOperationSupport(order = 60)
    @PutMapping(value = "/updateStatus")
    @LogAudit(moduleId = DictConstant.MODULE_ROLE, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> updateStatus(@RequestBody RoleDO param) {
        return roleService.updateStatus(param);
    }

    @ApiOperation(value = "所有角色")
    @GetMapping(value = "/all")
    @ApiOperationSupport(order = 70)
    public ResponseDO<List<RoleDO>> all() {
        return roleService.all();
    }

    @ApiOperation(value = "编辑页面菜单")
    @GetMapping(value = "/edit/menu")
    @ApiOperationSupport(order = 80)
    public ResponseDO<RoleDO> editMenu(RoleDO param) {
        return roleService.editMenu(param);
    }

    @ApiOperation(value = "详情用户组")
    @GetMapping(value = "/detail/group")
    @ApiOperationSupport(order = 90)
    public ResponseDO<List<GroupDO>> detailGroup(@Validated(value = RoleDO.ID.class) RoleDO param) {
        return roleService.detailGroup(param);
    }

    @ApiOperation(value = "详情菜单")
    @GetMapping(value = "/detail/menu")
    @ApiOperationSupport(order = 100, includeParameters = {"id"})
    public ResponseDO<List<MenuDO>> detailMenu(@Validated(value = RoleDO.ID.class) RoleDO param) {
        return roleService.detailMenu(param.getId());
    }
}