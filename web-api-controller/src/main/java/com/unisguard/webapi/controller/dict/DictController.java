package com.unisguard.webapi.controller.dict;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.service.dict.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "数据字典")
@RestController
@RequestMapping(value = "/dict")
public class DictController {
    @Resource
    private DictService dictService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<DictDO>> list(DictDO param) {
        return dictService.list(param);
    }

    @ApiOperation(value = "添加")
    @LogAudit(moduleId = DictConstant.MODULE_DICT, opType = DictConstant.OPT_ADD)
    @PostMapping(value = "/add")
    @ApiOperationSupport(order = 20)
    public ResponseDO<Long> add(@RequestBody @Validated(value = DictDO.Add.class) DictDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        return dictService.add(param);
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/detail")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    public ResponseDO<DictDO> detail(@Validated(value = DictDO.ID.class) DictDO param) {
        return dictService.detail(param.getId());
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    @LogAudit(moduleId = DictConstant.MODULE_DICT, opType = DictConstant.OPT_EDIT)
    @ApiOperationSupport(order = 40)
    public ResponseDO<Long> edit(@RequestBody @Validated(DictDO.Edit.class) DictDO param) {
        return dictService.edit(param);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    @LogAudit(moduleId = DictConstant.MODULE_DICT, opType = DictConstant.OPT_DEL)
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    public ResponseDO<Long> delete(@Validated(value = DictDO.ID.class) DictDO param) {
        return dictService.delete(param.getId());
    }

    @ApiOperation(value = "批量删除")
    @PostMapping(value = "/batch/delete")
    @ApiOperationSupport(order = 60)
    public ResponseDO<Boolean> batchDelete(@RequestBody List<Long> param) {
        return dictService.batchDelete(param);
    }

    @ApiOperation(value = "节点路径")
    @GetMapping(value = "/nodePath")
    @ApiOperationSupport(order = 70)
    public ResponseDO<List<Map<String, String>>> nodePath(DictDO param) {
        return dictService.nodePath(param);
    }

    @ApiOperation(value = "刷新")
    @GetMapping(value = "/refreshCache")
    @ApiOperationSupport(order = 80)
    public ResponseDO<Boolean> refreshCache() {
        return dictService.refreshCache();
    }

}
