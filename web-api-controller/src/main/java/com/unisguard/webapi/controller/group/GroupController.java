package com.unisguard.webapi.controller.group;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.group.GroupDO;
import com.unisguard.webapi.service.group.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户组管理")
@RestController
@RequestMapping(value = "/group")
public class GroupController {
    @Resource
    private GroupService groupService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<GroupDO>> list(GroupDO param) {
        return groupService.list(param);
    }

    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 20)
    @PostMapping(value = "/add")
    @LogAudit(moduleId = DictConstant.MODULE_USER_GROUP, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = GroupDO.Add.class) GroupDO param) {
        return groupService.add(param);
    }

    @ApiOperation(value = "详情")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    @GetMapping(value = "/detail")
    public ResponseDO<GroupDO> detail(@Validated(value = GroupDO.ID.class) GroupDO param) {
        return groupService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @ApiOperationSupport(order = 40)
    @PutMapping(value = "/edit")
    @LogAudit(moduleId = DictConstant.MODULE_USER_GROUP, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> edit(@RequestBody @Validated(GroupDO.Edit.class) GroupDO param) {
        return groupService.edit(param);
    }

    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @DeleteMapping(value = "/delete")
    @LogAudit(moduleId = DictConstant.MODULE_USER_GROUP, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = GroupDO.ID.class) GroupDO param) {
        return groupService.delete(param.getId());
    }

    @ApiOperation(value = "下拉列表")
    @GetMapping(value = "/queryList")
    @ApiOperationSupport(order = 60)
    public ResponseDO<List<GroupDO>> queryList() {
        return groupService.queryList();
    }

    @ApiOperation(value = "状态更新")
    @ApiOperationSupport(order = 90)
    @PutMapping(value = "/updateStatus")
    @LogAudit(moduleId = DictConstant.MODULE_USER_GROUP, opType = DictConstant.OPT_EDIT)
    public ResponseDO<Long> updateStatus(@RequestBody GroupDO param) {
        return groupService.updateStatus(param);
    }
}