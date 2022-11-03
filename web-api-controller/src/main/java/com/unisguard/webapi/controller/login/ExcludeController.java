package com.unisguard.webapi.controller.login;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import com.unisguard.webapi.service.exclude.ExcludeService;
import com.unisguard.webapi.service.sysconfig.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangshan
 * @date 2022/1/13 16:37
 */
@Api(tags = "无需授权接口")
@RestController
@RequestMapping(value = "/exclude")
public class ExcludeController {
    @Resource
    private ExcludeService exculdeService;
    @Resource
    private SysConfigService sysConfigService;

    /**
     * 导入授权
     *
     * @return
     */
    @ApiOperation(value = "系统授权导入授权")
    @ApiOperationSupport(order = 10)
    @PostMapping(value = "/lic/upload")
    public ResponseDO upload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        return exculdeService.upload(file);
    }

    /**
     * 下载申请
     *
     * @return
     */
    @ApiOperation(value = "系统授权下载申请")
    @ApiOperationSupport(order = 20)
    @GetMapping(value = "/lic/download")
    public void download(HttpServletResponse response) throws Exception {
        exculdeService.download(response);
    }


    @ApiOperation(value = "检查授权")
    @ApiOperationSupport(order = 30)
    @PostMapping(value = "/lic/check")
    public ResponseDO<Map<String, Object>> checkLic() {
        if (GlobalConstant.LIC == null) {
            return ResponseDO.success();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("authCycle", GlobalConstant.LIC.getAuthCycle());
        map.put("authTime", GlobalConstant.LIC.getAuthTime());
        return ResponseDO.success(map);
    }

    @ApiOperation(value = "加载logo")
    @ApiOperationSupport(order = 40)
    @GetMapping(value = "/load/logo")
    public void loadLogoDataStream(HttpServletRequest request, HttpServletResponse response) {
        sysConfigService.loadLogoDataStream(request, response);
    }

    @ApiOperation(value = "加载系统配置")
    @ApiOperationSupport(order = 50)
    @GetMapping(value = "/load/config")
    public ResponseDO<List<SysConfigDO>> loadSysNameAndCopyRight() {
        return sysConfigService.loadSysNameAndCopyRight();
    }
}
