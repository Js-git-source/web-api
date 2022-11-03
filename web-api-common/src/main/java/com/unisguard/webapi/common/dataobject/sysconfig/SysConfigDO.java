package com.unisguard.webapi.common.dataobject.sysconfig;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @description 系统配置实体类
 * @author sunsaike
 */
@Data
@ApiModel(value = "系统配置")
public class SysConfigDO extends BaseDO {

    @ApiModelProperty(value = "配置标识", position = 20)
    @NotNull(message = "配置标识不能为空", groups = {Add.class, Edit.class})
    private Integer dataKey;

    @ApiModelProperty(value = "配置内容", position = 30)
    @NotBlank(message = "配置内容不能为空", groups = {Add.class, Edit.class})
    private String dataValue;
}