package com.unisguard.webapi.controller.upgrade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.upgrade.UpgradeDO;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.service.upgrade.UpgradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统升级")
@RestController
@RequestMapping(value = "/upgrade")
public class UpgradeController {
    @Resource
    private UpgradeService upgradeService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<UpgradeDO>> list(UpgradeDO param) {
        return upgradeService.list(param);
    }


    @ApiOperation(value = "在线升级")
    @ApiOperationSupport(order = 30)
    @PostMapping(value = "/upload", headers = "content-type=multipart/*")
    @LogAudit(moduleId = DictConstant.MODULE_UPGRADE, opType = DictConstant.OPT_UPLOAD)
    public ResponseDO<Long> upload(@RequestParam(value = "file") MultipartFile file,
                                   @Validated(value = UpgradeDO.Add.class) UpgradeDO param) throws Exception {
        param.setUserId(CurrentUserUtil.getId());
        return upgradeService.upload(file, param);
    }
}