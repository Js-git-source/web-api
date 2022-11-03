package com.unisguard.webapi.controller.mon;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.mon.MonHisDO;
import com.unisguard.webapi.service.mon.MonHisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "系统监控记录管理")
@RestController
@RequestMapping(value = "/mon/his")
public class MonHisController {
    @Resource
    private MonHisService monHisService;

    @ApiOperation(value = "中间件详细列表")
    @ApiOperationSupport(order = 10)
    @GetMapping(value = "/middleware/detail/list")
    public ResponseDO<List<MonHisDO>> middlewareDetailList(MonHisDO param) {
        return monHisService.middlewareDetailList(param);
    }

    @ApiOperation(value = "分析中心流量")
    @ApiOperationSupport(order = 20)
    @GetMapping(value = "/analysis/flow")
    public ResponseDO<Map<String, List<MonHisDO>>> analysisFlow(MonHisDO param) {
        return monHisService.analysisFlow(param);
    }
}