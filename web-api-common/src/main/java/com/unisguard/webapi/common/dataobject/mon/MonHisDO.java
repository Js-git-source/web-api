package com.unisguard.webapi.common.dataobject.mon;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "系统监控记录")
public class MonHisDO extends BaseDO {
    @ApiModelProperty(value = "监控类别（1001400）", position = 10)
    @NotNull(message = "监控类别（1001400）不能为空", groups = {Add.class, Edit.class})
    private Integer category;

    @ApiModelProperty(value = "监控类型（1001100|1001500|1001600）", position = 20)
    @NotNull(message = "监控类型（1001100|1001500|1001600）不能为空", groups = {Add.class, Edit.class})
    private Integer type;

    @ApiModelProperty(value = "指标类型", position = 30)
    @NotNull(message = "指标类型不能为空", groups = {Add.class, Edit.class})
    private Integer quota;

    @ApiModelProperty(value = "IP地址", position = 40)
    @NotBlank(message = "IP地址不能为空", groups = {Add.class, Edit.class})
    private String ip;

    @ApiModelProperty(value = "CPU使用率", position = 50)
    @NotNull(message = "CPU使用率不能为空", groups = {Add.class, Edit.class})
    private Integer cpuRate = 0;

    @ApiModelProperty(value = "GC—CPU使用率", position = 60)
    @NotNull(message = "GC—CPU使用率不能为空", groups = {Add.class, Edit.class})
    private Integer gcRate= 0;

    @ApiModelProperty(value = "内存使用", position = 70)
    @NotNull(message = "内存使用不能为空", groups = {Add.class, Edit.class})
    private Long memUse= 0L;

    @ApiModelProperty(value = "内存当前分配大小", position = 80)
    @NotNull(message = "内存当前分配大小不能为空", groups = {Add.class, Edit.class})
    private Long memLocal= 0L;

    @ApiModelProperty(value = "内存使用率", position = 90)
    @NotNull(message = "内存使用率不能为空", groups = {Add.class, Edit.class})
    private Integer memRate= 0;

    @ApiModelProperty(value = "内存碎片", position = 100)
    @NotNull(message = "内存碎片不能为空", groups = {Add.class, Edit.class})
    private Long memFrag= 0L;

    @ApiModelProperty(value = "当前线程数", position = 110)
    @NotNull(message = "当前线程数不能为空", groups = {Add.class, Edit.class})
    private Integer threadCnt= 0;

    @ApiModelProperty(value = "创建连接数", position = 120)
    @NotNull(message = "创建连接数不能为空", groups = {Add.class, Edit.class})
    private Integer sessionNew= 0;

    @ApiModelProperty(value = "缓存连接数", position = 130)
    @NotNull(message = "缓存连接数不能为空", groups = {Add.class, Edit.class})
    private Integer sessionCache= 0;

    @ApiModelProperty(value = "活动连接数", position = 140)
    @NotNull(message = "活动连接数不能为空", groups = {Add.class, Edit.class})
    private Integer sessionActive= 0;

    @ApiModelProperty(value = "当前连接数", position = 150)
    @NotNull(message = "当前连接数不能为空", groups = {Add.class, Edit.class})
    private Integer sessionCnt= 0;

    @ApiModelProperty(value = "IOPS", position = 160)
    @NotNull(message = "IOPS不能为空", groups = {Add.class, Edit.class})
    private Integer iops= 0;

    @ApiModelProperty(value = "QPS", position = 170)
    @NotNull(message = "QPS不能为空", groups = {Add.class, Edit.class})
    private Integer qps= 0;

    @ApiModelProperty(value = "TPS", position = 180)
    @NotNull(message = "TPS不能为空", groups = {Add.class, Edit.class})
    private Integer tps= 0;

    @ApiModelProperty(value = "读", position = 190)
    @NotNull(message = "读不能为空", groups = {Add.class, Edit.class})
    private Long ioRead= 0L;

    @ApiModelProperty(value = "写", position = 200)
    @NotNull(message = "写不能为空", groups = {Add.class, Edit.class})
    private Long ioWrite= 0L;

    @ApiModelProperty(value = "客户端数量", position = 210)
    @NotNull(message = "客户端数量不能为空", groups = {Add.class, Edit.class})
    private Integer clientCnt= 0;

    @ApiModelProperty(value = "slave数量", position = 220)
    @NotNull(message = "slave数量不能为空", groups = {Add.class, Edit.class})
    private Integer slaveCnt= 0;

    @ApiModelProperty(value = "key数量", position = 230)
    @NotNull(message = "key数量不能为空", groups = {Add.class, Edit.class})
    private Integer keyCnt= 0;

    @ApiModelProperty(value = "获取", position = 240)
    @NotNull(message = "获取不能为空", groups = {Add.class, Edit.class})
    private Long ioFetch= 0L;

    @ApiModelProperty(value = "合并", position = 250)
    @NotNull(message = "合并不能为空", groups = {Add.class, Edit.class})
    private Long ioMerge= 0L;;

    @ApiModelProperty(value = "topic", position = 260)
    @NotBlank(message = "topic不能为空", groups = {Add.class, Edit.class})
    private String topic;

    @ApiModelProperty(value = "group id", position = 270)
    @NotBlank(message = "group id不能为空", groups = {Add.class, Edit.class})
    private String groupId;

    @ApiModelProperty(value = "total lag", position = 280)
    @NotNull(message = "total lag不能为空", groups = {Add.class, Edit.class})
    private Long lagTotal= 0L;

    @ApiModelProperty(value = "生产速度", position = 290)
    @NotNull(message = "生产速度不能为空", groups = {Add.class, Edit.class})
    private Long produceSpeed= 0L;

    @ApiModelProperty(value = "消费速度", position = 300)
    @NotNull(message = "消费速度不能为空", groups = {Add.class, Edit.class})
    private Long consumeSpeed= 0L;

    @ApiModelProperty(value = "速率", position = 310)
    @NotNull(message = "速率不能为空", groups = {Add.class, Edit.class})
    private Integer rate= 0;

    @ApiModelProperty(value = "积压天数", position = 320)
    @NotNull(message = "积压天数不能为空", groups = {Add.class, Edit.class})
    private Long lagDay= 0L;

    @ApiModelProperty(value = "网卡名称", position = 330)
    @NotBlank(message = "网卡名称不能为空", groups = {Add.class, Edit.class})
    private String netInter;

    @ApiModelProperty(value = "网卡流量 ", position = 350)
    @NotNull(message = "网卡流量 不能为空", groups = {Add.class, Edit.class})
    private Long netInterCnt= 0L;

    @ApiModelProperty(value = "磁盘使用（JSON）", position = 360)
    private String diskJson;

    @ApiModelProperty(value = "负载", position = 370)
    private String sysLoad;

    @ApiModelProperty(value = "负载5分钟", position = 380)
    private String sysLoad5;

    @ApiModelProperty(value = "负载15分钟", position = 390)
    private String sysLoad15;

    @ApiModelProperty(value = "系统监控结果", position = 400)
    private List<MonHisDO> monHisDOList;

    @ApiModelProperty(value = "查询开始时间", position = 410)
    private LocalDateTime startTime = LocalDateTime.now().minusHours(1).minusMinutes(5);

    @ApiModelProperty(value = "查询结束时间", position = 420)
    private LocalDateTime endTime = LocalDateTime.now();
}