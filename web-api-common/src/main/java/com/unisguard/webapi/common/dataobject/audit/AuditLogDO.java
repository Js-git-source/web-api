package com.unisguard.webapi.common.dataobject.audit;

import com.unisguard.webapi.common.annotation.JSONDict;
import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(value = "系统审计日志")
public class AuditLogDO {

    /**
     * UUID
     */
    private String data_id;

    /**
     * 服务/引擎编号
     */
    private Integer engine_id;

    /**
     * 模块编号
     */
    private Integer module_id;
    /**
     * 操作用户编号
     */
    private Integer user_id;

    /**
     * 操作用户姓名
     */
    private String user_name;
    /**
     * 操作用户账号
     */
    private String user_acc;
    /**
     * 操作时间
     */
    private Date op_time;
    /**
     * 操作类型编号
     */
    private Integer op_type;
    /**
     * 操作结果编号
     */
    private Integer op_ret;
    /**
     * 客户端IP
     */
    private String client_ip;
    /**
     * 日志内容
     */
    private String msg;

    public AuditLogDO(){

    }

    public AuditLogDO(AuditDO auditDO){
        this.data_id = auditDO.getDataId();
        this.engine_id = auditDO.getEngineId();
        this.module_id = auditDO.getModuleId();
        this.user_id = auditDO.getUserId();
        this.user_name = auditDO.getUserName();
        this.user_acc = auditDO.getUserAcc();
        this.op_time = auditDO.getOpTime();
        this.op_type = auditDO.getOpType();
        this.op_ret = auditDO.getOpRet();
        this.client_ip = auditDO.getClientIp();
        this.msg = auditDO.getMsg();
    }


}