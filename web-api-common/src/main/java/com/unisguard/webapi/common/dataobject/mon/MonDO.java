package com.unisguard.webapi.common.dataobject.mon;

import com.unisguard.webapi.common.annotation.JSONDict;
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
@ApiModel(value = "系统监控")
public class MonDO extends BaseDO {
    @ApiModelProperty(value = "监控类别（1001400）", position = 10)
    private Integer category;

    @ApiModelProperty(value = "监控类型（1001100|1001500|1001600）", position = 20)
    private Integer type;

    @ApiModelProperty(value = "IP地址", position = 30)
    private String ip;

    @ApiModelProperty(value = "版本", position = 40)
    private String version;

    @ApiModelProperty(value = "内存最大空间", position = 50)
    private Long memMax;

    @ApiModelProperty(value = "内存当前分配大小", position = 60)
    private Long memLocal;

    @ApiModelProperty(value = "内存使用", position = 70)
    private Long memUse;

    @ApiModelProperty(value = "内存使用率", position = 80)
    private Integer memRate;

    @ApiModelProperty(value = "内存碎片", position = 90)
    private Long memFrag;

    @ApiModelProperty(value = "CPU使用率", position = 100)
    private Integer cpuRate;

    @ApiModelProperty(value = "GC—CPU使用率", position = 110)
    private Integer gcRate;

    @ApiModelProperty(value = "最大线程数", position = 120)
    private Integer threadMax;

    @ApiModelProperty(value = "当前线程数", position = 130)
    private Integer threadCnt;

    @ApiModelProperty(value = "繁忙线程数", position = 140)
    private Integer threadBusy;

    @ApiModelProperty(value = "最大连接数", position = 150)
    private Integer sessionMax;

    @ApiModelProperty(value = "当前连接数", position = 160)
    private Integer sessionCnt;

    @ApiModelProperty(value = "磁盘大小", position = 170)
    private Long diskTotal;

    @ApiModelProperty(value = "磁盘使用", position = 180)
    private Long diskUse;

    @ApiModelProperty(value = "客户端数量", position = 190)
    private Integer clientCnt;

    @ApiModelProperty(value = "slave数量", position = 200)
    private Integer slaveCnt;

    @ApiModelProperty(value = "key数量", position = 210)
    private Integer keyCnt;

    @ApiModelProperty(value = "系统负载", position = 220)
    private Long sysLoad;

    @ApiModelProperty(value = "启动时长", position = 230)
    private Long uptime;

    @ApiModelProperty(value = "Topic", position = 240)
    private String topic;

    @ApiModelProperty(value = "GroupID", position = 250)
    private String groupId;

    @ApiModelProperty(value = "Total Lag", position = 260)
    private Long lagTotal;

    @ApiModelProperty(value = "生产速度", position = 270)
    private Long produceSpeed;

    @ApiModelProperty(value = "消费速度", position = 280)
    private Long consumeSpeed;

    @ApiModelProperty(value = "效率", position = 290)
    private Integer rate;

    @ApiModelProperty(value = "积压天数", position = 300)
    private Long lagDay;

    @ApiModelProperty(value = "名称", position = 310)
    private String name;

    @ApiModelProperty(value = "定时任务", position = 320)
    private String crontab;

    @JSONDict(codeType = 1001000)
    @ApiModelProperty(value = "结果(1001000)", position = 330)
    private Integer ret;

    @ApiModelProperty(value = "磁盘使用（JSON）", position = 350)
    private String diskJson;

    @ApiModelProperty(value = "描述", position = 360)
    private String description;

    @ApiModelProperty(value = "系统监控记录", position = 360)
    private List<MonHisDO> monHisDOList;

    @ApiModelProperty(value = "查询开始时间", position = 370)
    private LocalDateTime startTime = LocalDateTime.now().minusHours(1);

    @ApiModelProperty(value = "查询结束时间", position = 380)
    private LocalDateTime endTime = LocalDateTime.now();

    @ApiModelProperty(value = "系统监控记录(数据流量)", position = 360)
    private List<List<MonHisDO>> monHisDOLists;
}