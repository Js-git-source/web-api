package com.unisguard.webapi.common.dataobject.menu;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "菜单接口")
public class MenuUrlDO extends BaseDO {
    @ApiModelProperty(value = "菜单编号", position = 10)
    @NotNull(message = "菜单编号不能为空", groups = {Add.class, Edit.class})
    private Long menuId;

    @ApiModelProperty(value = "接口url", position = 20)
    @NotBlank(message = "接口url不能为空", groups = {Add.class, Edit.class})
    private String url;

    @ApiModelProperty(value = "接口名称", position = 30)
    private String name;
}