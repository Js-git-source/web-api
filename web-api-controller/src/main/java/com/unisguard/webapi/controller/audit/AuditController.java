package com.unisguard.webapi.controller.audit;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.audit.AuditDO;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.service.audit.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "审计日志")
@RestController
@RequestMapping(value = "/audit")
public class AuditController {
    @Resource
    private AuditService auditService;

    @ApiOperation(value = "列表")
    @ApiOperationSupport(order = 10)
    @GetMapping(value = "/list")
    public ResponseDO<List<AuditDO>> list(AuditDO param) throws Exception {
        return auditService.list(param);
    }

    @ApiOperation(value = "详情")
    @ApiOperationSupport(order = 20, includeParameters = {"esId"})
    @GetMapping(value = "/detail")
    public ResponseDO<AuditDO> detail(@Validated(value = AuditDO.ESID.class) AuditDO param) throws Exception {
        return auditService.detail(param.getEsId());
    }

    @ApiOperation(value = "导出")
    @ApiOperationSupport(order = 30)
    @GetMapping(value = "/download")
    @LogAudit(moduleId = DictConstant.MODULE_AUDIT, opType = DictConstant.OPT_DOWNLOAD)
    public ResponseDO download(HttpServletResponse response, AuditDO param) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String excelName = URLEncoder.encode(GlobalConstant.MODULE_AUDIT_NAME, GlobalConstant.UTF_8);  // 这里URLEncoder.encode可以防止中文乱码
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        return auditService.download(response.getOutputStream(), param);
    }

    @ApiOperation(value = "清空")
    @ApiOperationSupport(order = 40)
    @DeleteMapping(value = "/clear")
    @LogAudit(moduleId = DictConstant.MODULE_AUDIT, opType = DictConstant.OPT_CLEAR)
    public ResponseDO clear(AuditDO param) throws Exception {
        return auditService.clear(param);
    }
}