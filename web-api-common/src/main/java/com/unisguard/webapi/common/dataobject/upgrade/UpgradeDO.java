package com.unisguard.webapi.common.dataobject.upgrade;

import com.unisguard.webapi.common.annotation.JSONDict;
import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "系统升级")
public class UpgradeDO extends BaseDO {
    /**
     * 版本号的生成规则是什么
     */
    @ApiModelProperty(value = "版本号", position = 20)
    private String version;

    @ApiModelProperty(value = "升级包文件路径", position = 20)
    private String path;

    @JSONDict(codeType = 1002100)
    @ApiModelProperty(value = "升级结果", position = 30)
    private Integer status;

    @JSONDict(codeType = 1001800)
    @ApiModelProperty(value = "升级方式", position = 40)
    private Integer type;

    @ApiModelProperty(value = "升级操作人编码", position = 50)
    private Long userId;

    @ApiModelProperty(value = "升级操作人姓名", position = 60)
    private String userName;

    @ApiModelProperty(value = "升级描述", position = 70)
    private String description;

    @ApiModelProperty(value = "查询条件-升级时间", position = 80)
    private String param_time;
    @ApiModelProperty(value = "开始时间", position = 90)
    private String start_date;
    @ApiModelProperty(value = "结束时间", position = 100)
    private String end_date;

    @ApiModelProperty(value = "账号", position = 110)
    private String account;

}