package com.unisguard.webapi.controller.sysconfig;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.annotation.LogAudit;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigVO;
import com.unisguard.webapi.service.sysconfig.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hyperic.sigar.NetInterfaceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sunsaike
 * @description 系统配置
 */
@Api(tags = "系统配置")
@RestController
@RequestMapping(value = "/sys/config")
public class SysConfigController {
    @Resource
    private SysConfigService sysConfigService;

    @Value("${upload.logo.path}")
    private String uploadLogoPath;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<SysConfigDO>> list(SysConfigDO param) {
        return sysConfigService.list(param);
    }

    @ApiOperation(value = "添加或编辑（dataKey已存在则更新）")
    @PostMapping(value = "/add")
    @ApiOperationSupport(order = 20)
    @LogAudit(moduleId = DictConstant.MODULE_SYSTEM, opType = DictConstant.OPT_ADD)
    public ResponseDO<Long> add(@RequestBody @Validated(value = SysConfigDO.Add.class) SysConfigDO param) {
        return sysConfigService.add(param);
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/detail")
    @ApiOperationSupport(order = 30, includeParameters = {"id"})
    public ResponseDO<SysConfigDO> detail(@Validated(value = SysConfigDO.ID.class) SysConfigDO param) {
        return sysConfigService.detail(param.getId());
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    @ApiOperationSupport(order = 50, includeParameters = {"id"})
    @LogAudit(moduleId = DictConstant.MODULE_SYSTEM, opType = DictConstant.OPT_DEL)
    public ResponseDO<Long> delete(@Validated(value = SysConfigDO.ID.class) SysConfigDO param) {
        return sysConfigService.delete(param.getId());
    }

    @ApiOperation(value = "网卡信息")
    @GetMapping(value = "/network/card")
    @ApiOperationSupport(order = 60)
    public ResponseDO<List<NetInterfaceConfig>> network() {
        return sysConfigService.network();
    }

    @ApiOperation(value = "DNS配置")
    @PostMapping(value = "/dns")
    @ApiOperationSupport(order = 70)
    public ResponseDO<String> ifConfig(@RequestBody @Validated(value = SysConfigVO.DnsConfig.class) SysConfigVO param) {
        return sysConfigService.configDns(param);
    }

    @ApiOperation(value = "网络测试")
    @PostMapping(value = "/network/test")
    @ApiOperationSupport(order = 80)
    public ResponseDO<String> testNetwork(@RequestBody @Validated(value = SysConfigVO.NetworkTest.class) SysConfigVO param) {
        return sysConfigService.testNetwork(param);
    }

    @ApiOperation(value = "添加(批量)")
    @PostMapping(value = "/batch/add")
    @ApiOperationSupport(order = 90)
    @LogAudit(moduleId = DictConstant.MODULE_SYSTEM, opType = DictConstant.OPT_ADD)
    public ResponseDO<String> batchAdd(@RequestBody List<SysConfigDO> param) {
        return sysConfigService.batchAdd(param);
    }

    @ApiOperation(value = "邮件服务测试")
    @PostMapping(value = "/mail/test")
    @ApiOperationSupport(order = 100)
    public ResponseDO<Boolean> mailTest(@RequestBody @Validated(value = SysConfigVO.MailTest.class) SysConfigVO param) {
        return sysConfigService.mailTest(param);
    }

    @ApiOperation(value = "时间同步-获取服务器时间")
    @GetMapping(value = "/time/sync/system")
    @ApiOperationSupport(order = 110)
    public ResponseDO<String> getSystemTime() {
        return sysConfigService.getSystemTime();
    }

    @ApiOperation(value = "时间同步-立即同步")
    @PostMapping(value = "/time/sync/immediate")
    @ApiOperationSupport(order = 120)
    public ResponseDO<String> rightNowSyncTime(@RequestBody @Validated(value = SysConfigVO.NowNtp.class) SysConfigVO param) {
        return sysConfigService.rightNowSyncTime(param);
    }

    @ApiOperation(value = "时间同步-周期同步")
    @PostMapping(value = "/time/sync/cycle")
    @ApiOperationSupport(order = 130)
    public ResponseDO<String> syncTime(@RequestBody @Validated(value = SysConfigVO.Ntp.class) SysConfigVO param) {
        return sysConfigService.syncTime(param);
    }

    @ApiOperation(value = "上传logo")
    @PostMapping(value = "/logo/upload")
    @ApiOperationSupport(order = 140)
    public ResponseDO<String> uploadLogo(@RequestBody MultipartFile file, HttpServletRequest request) {
        return sysConfigService.uploadLogo(file, request, uploadLogoPath);
    }

    @ApiOperation(value = "加载logo")
    @GetMapping(value = "/logo/load")
    @ApiOperationSupport(order = 150)
    public void loadLogoDataStream(HttpServletRequest request, HttpServletResponse response) {
        sysConfigService.loadLogoDataStream(request, response);
    }
}