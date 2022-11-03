package com.unisguard.webapi.common.dataobject.deploy;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "系统部署信息")
public class DeployDO extends BaseDO {
    @ApiModelProperty(value = "部署类别(1001400)", position = 10)
    private Integer category;

    @ApiModelProperty(value = "引擎服务类别(1001100|1001500|1001600)", position = 20)
    private Integer type;

    @ApiModelProperty(value = "IP地址", position = 30)
    @NotBlank(message = "IP地址不能为空", groups = {Add.class, Edit.class})
    private String ip;
}