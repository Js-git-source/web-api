package com.unisguard.webapi.common.dataobject.audit;

import com.unisguard.webapi.common.annotation.JSONDict;
import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "审计日志")
public class AuditDO extends BaseDO {

    /**
     * 用于es删除
     */
    @ApiModelProperty(value = "ES主键", position = 2)
    @NotNull(message = "ES主键不能为空", groups = {ESID.class})
    private String esId;

    public interface ESID{

    }

    /**
     * UUID
     */
    @ApiModelProperty(value = "UUID", position = 10)
    private String dataId;

    /**
     * 服务/引擎编号
     */
    @JSONDict(codeType = 1001100)
    @ApiModelProperty(value = "服务/引擎编号", position = 20)
    private Integer engineId;
    /**
     * 模块编号
     */
    @JSONDict(codeType = 1001200)
    @ApiModelProperty(value = "模块编号", position = 30)
    private Integer moduleId;
    /**
     * 操作用户编号
     */
    @ApiModelProperty(value = "操作用户编号", position = 40)
    private Integer userId;
    /**
     * 操作用户姓名
     */
    @ApiModelProperty(value = "操作用户姓名", position = 50)
    private String userName;
    /**
     * 操作用户账号
     */
    @ApiModelProperty(value = "操作用户账号", position = 60)
    private String userAcc;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", position = 70)
    private Date opTime;
    /**
     * 操作类型编号
     */
    @JSONDict(codeType = 1001300)
    @ApiModelProperty(value = "操作类型编号", position = 80)
    private Integer opType;
    /**
     * 操作结果编号
     */
    @JSONDict(codeType = 1001000)
    @ApiModelProperty(value = "操作结果编号", position = 90)
    private Integer opRet;
    /**
     * 客户端IP
     */
    @ApiModelProperty(value = "客户端IP", position = 100)
    private String clientIp;
    /**
     * 日志内容
     */
    @ApiModelProperty(value = "日志内容", position = 110)
    private String msg;
    /**
     * 操作时间-参数查询
     */
    @ApiModelProperty(value = "操作时间-参数查询", position = 110)
    private String optTimeStr;
    /**
     * 排序方式
     */
    @ApiModelProperty(value = "排序方式", position = 120)
    private String sortOrder;



}