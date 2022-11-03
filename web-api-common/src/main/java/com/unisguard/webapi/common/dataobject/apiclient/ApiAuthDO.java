package com.unisguard.webapi.common.dataobject.apiclient;

import com.unisguard.webapi.common.dataobject.api.ApiDO;
import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @description api管理-授权实体
 * @author sunsaike
 */
@Data
@ApiModel(value = "系统API授权")
public class ApiAuthDO extends ApiDO {
    @ApiModelProperty(value = "客户端编号", position = 10)
    @NotNull(message = "客户端编号不能为空", groups = {Add.class, Edit.class})
    private Long clientId;

    @ApiModelProperty(value = "API编号", position = 20)
    @NotNull(message = "API编号不能为空", groups = {Add.class, Edit.class})
    private Long apiId;
}