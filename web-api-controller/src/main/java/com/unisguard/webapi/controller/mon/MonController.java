package com.unisguard.webapi.controller.mon;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.mon.MonDO;
import com.unisguard.webapi.service.mon.MonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统监控")
@RestController
@RequestMapping(value = "/mon")
public class MonController {
    @Resource
    private MonService monService;

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    @ApiOperationSupport(order = 10)
    public ResponseDO<List<MonDO>> list(MonDO param) {
        return monService.list(param);
    }

    @ApiOperation(value = "Web中间件监控(cpu)")
    @ApiOperationSupport(order = 20)
    @GetMapping(value = "/web/cpu")
    public ResponseDO<MonDO> cpu(MonDO param) {
        return monService.cpu(param);
    }

    @ApiOperation(value = "Web中间件监控(内存)")
    @ApiOperationSupport(order = 30)
    @GetMapping(value = "/web/ram")
    public ResponseDO<MonDO> ram(MonDO param) {
        return monService.ram(param);
    }

    @ApiOperation(value = "Web中间件监控(线程)")
    @ApiOperationSupport(order = 40)
    @GetMapping(value = "/web/thread")
    public ResponseDO<MonDO> thread(MonDO param) {
        return monService.thread(param);
    }

    @ApiOperation(value = "Web中间件监控(连接)")
    @ApiOperationSupport(order = 50)
    @GetMapping(value = "/web/connect")
    public ResponseDO<MonDO> connect(MonDO param) {
        return monService.connect(param);
    }

    @ApiOperation(value = "数据库监控连接数")
    @ApiOperationSupport(order = 60)
    @GetMapping(value = "/database/connect")
    public ResponseDO<MonDO> databaseConnect(MonDO param) {
        return monService.databaseConnect(param);
    }

    @ApiOperation(value = "数据库监控吞吐量")
    @ApiOperationSupport(order = 70)
    @GetMapping(value = "/database/throughput")
    public ResponseDO<MonDO> throughput(MonDO param) {
        return monService.throughput(param);
    }

    @ApiOperation(value = "数据库监控io")
    @ApiOperationSupport(order = 80)
    @GetMapping(value = "/database/io")
    public ResponseDO<MonDO> io(MonDO param) {
        return monService.io(param);
    }

    @ApiOperation(value = "数据库监控网络")
    @ApiOperationSupport(order = 90)
    @GetMapping(value = "/database/network")
    public ResponseDO<MonDO> network(MonDO param) {
        return monService.network(param);
    }

    @ApiOperation(value = "引擎服务监控")
    @ApiOperationSupport(order = 100)
    @GetMapping(value = "/engine/list")
    public ResponseDO<List<MonDO>> getEngineList(MonDO param) {
        return monService.getEngineList(param);
    }

    @ApiOperation(value = "索引列表")
    @ApiOperationSupport(order = 110)
    @GetMapping(value = "/index/list")
    public ResponseDO<List<MonDO>> indexList(MonDO param) {
        return monService.indexList(param);
    }

    @ApiOperation(value = "索引监控列表搜索索引性能")
    @ApiOperationSupport(order = 120)
    @GetMapping(value = "/index/performance/search")
    public ResponseDO<MonDO> search(MonDO param) {
        return monService.search(param);
    }

    @ApiOperation(value = "缓存详情")
    @ApiOperationSupport(order = 130)
    @GetMapping(value = "/redis/detail")
    public ResponseDO<MonDO> redisDetail(MonDO param) {
        return monService.redisDetail(param);
    }

    @ApiOperation(value = "中间件列表")
    @ApiOperationSupport(order = 140)
    @GetMapping(value = "/middleware/list")
    public ResponseDO<List<MonDO>> middlewareList(MonDO param) {
        return monService.middlewareList(param);
    }

    @ApiOperation(value = "服务节点监控")
    @ApiOperationSupport(order = 150)
    @GetMapping(value = "/service/list")
    public ResponseDO<MonDO> serviceList(MonDO param) {
        return monService.serviceList(param);
    }

    @ApiOperation(value = "缓存监控cpu使用率")
    @ApiOperationSupport(order = 160)
    @GetMapping(value = "/cache/usage/cpu")
    public ResponseDO<MonDO> usageCpu(MonDO param) {
        return monService.usageCpu(param);
    }

    @ApiOperation(value = "缓存监控内存使用率")
    @ApiOperationSupport(order = 170)
    @GetMapping(value = "/cache/usage/ram")
    public ResponseDO<MonDO> usageRam(MonDO param) {
        return monService.usageRam(param);
    }

    @ApiOperation(value = "缓存监控Key数量监控")
    @ApiOperationSupport(order = 180)
    @GetMapping(value = "/cache/key")
    public ResponseDO<MonDO> key(MonDO param) {
        return monService.key(param);
    }

    @ApiOperation(value = "消息中间件监控topic监控")
    @ApiOperationSupport(order = 190)
    @GetMapping(value = "/middleware/topic")
    public ResponseDO<List<MonDO>>  topic(MonDO param) {
        return monService.topic(param);
    }

    @ApiOperation(value = "消息中间件监控流量监控")
    @ApiOperationSupport(order = 200)
    @GetMapping(value = "/middleware/flow")
    public ResponseDO<MonDO> flow(MonDO param) {
        return monService.flow(param);
    }

    @ApiOperation(value = "分析中心cpu使用率")
    @ApiOperationSupport(order = 210)
    @GetMapping(value = "/analysis/usage/cpu")
    public ResponseDO<MonDO> analysisUsageCpu(MonDO param) {
        return monService.analysisUsageCpu(param);
    }

    @ApiOperation(value = "分析中心内存使用率")
    @ApiOperationSupport(order = 220)
    @GetMapping(value = "/analysis/usage/ram")
    public ResponseDO<MonDO> analysisUsageRam(MonDO param) {
        return monService.analysisUsageRam(param);
    }

    @ApiOperation(value = "分析中心负载")
    @ApiOperationSupport(order = 230)
    @GetMapping(value = "/analysis/load")
    public ResponseDO<MonDO> analysisLoad(MonDO param) {
        return monService.analysisLoad(param);
    }
}