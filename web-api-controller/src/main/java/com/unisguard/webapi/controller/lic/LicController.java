package com.unisguard.webapi.controller.lic;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.lic.LicDO;
import com.unisguard.webapi.service.lic.LicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "系统授权")
@RestController
@RequestMapping(value = "/lic")
public class LicController {
    @Resource
    private LicService licService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<LicDO>> list(LicDO param) {
        return licService.list(param);
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/last")
    @ApiOperationSupport(order = 20)
    public ResponseDO<LicDO> last() {
        return licService.last();
    }

    @ApiOperation(value = "导入授权")
    @ApiOperationSupport(order = 30)
    @PostMapping(value = "/upload")
    @LogAudit(moduleId = DictConstant.MODULE_GRANT, opType = DictConstant.OPT_UPLOAD)
    public ResponseDO upload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        return licService.upload(file);
    }

    @ApiOperation(value = "下载申请")
    @ApiOperationSupport(order = 40)
    @GetMapping(value = "/download")
    @LogAudit(moduleId = DictConstant.MODULE_GRANT, opType = DictConstant.OPT_DOWNLOAD)
    public void download(HttpServletResponse response) throws Exception {
        licService.download(response);
    }
}