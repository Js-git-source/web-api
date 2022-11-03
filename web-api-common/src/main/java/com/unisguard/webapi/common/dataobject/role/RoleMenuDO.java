package com.unisguard.webapi.common.dataobject.role;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "角色菜单配置")
public class RoleMenuDO extends BaseDO {
    @ApiModelProperty(value = "角色编号", position = 10)
    @NotNull(message = "角色编号不能为空", groups = {Add.class, Edit.class})
    private Long roleId;

    @ApiModelProperty(value = "菜单编号", position = 20)
    @NotNull(message = "菜单编号不能为空", groups = {Add.class, Edit.class})
    private Long menuId;
}