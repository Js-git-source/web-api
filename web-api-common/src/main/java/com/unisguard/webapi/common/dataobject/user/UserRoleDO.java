package com.unisguard.webapi.common.dataobject.user;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "用户授权")
public class UserRoleDO extends BaseDO {
    @ApiModelProperty(value = "授权类型(1000400)", position = 10)
    @NotNull(message = "授权类型(1000400)不能为空", groups = {Add.class, Edit.class})
    private Integer type;

    @ApiModelProperty(value = "账号编号", position = 20)
    @NotNull(message = "账号编号不能为空", groups = {Add.class, Edit.class})
    private Long userId;

    @ApiModelProperty(value = "角色编号", position = 30)
    @NotNull(message = "角色编号不能为空", groups = {Add.class, Edit.class})
    private Long roleId;

    @ApiModelProperty(value = "角色名称", position = 40)
    private String roleName;
}