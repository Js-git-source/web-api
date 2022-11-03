package com.unisguard.webapi.common.dataobject.group;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "用户组授权")
public class GroupRoleDO extends BaseDO {
    @ApiModelProperty(value = "用户组编号", position = 10)
    @NotNull(message = "用户组编号不能为空", groups = {Add.class, Edit.class})
    private Long groupId;

    @ApiModelProperty(value = "角色编号", position = 20)
    @NotNull(message = "角色编号不能为空", groups = {Add.class, Edit.class})
    private Long roleId;

    @ApiModelProperty(value = "角色名称", position = 30)
    private String roleName;

}