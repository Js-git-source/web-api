package com.unisguard.webapi.common.dataobject.role;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "角色")
public class RoleDO extends BaseDO {
    @ApiModelProperty(value = "角色名称", position = 10)
    @NotBlank(message = "角色名称不能为空", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 20, message = "角色名称长度应在1-20之间", groups = {Add.class, Edit.class})
    @Pattern(regexp = "^[A-Za-z0-9\u4e00-\u9fa5]+$", message = "角色名称只允许中文或者大小写英文字母数字", groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "角色类型", position = 20)
    @NotNull(message = "角色类型不能为空", groups = {Add.class, Edit.class})
    private Integer type;

    @ApiModelProperty(value = "角色状态", position = 30)
    @NotNull(message = "角色状态不能为空", groups = {Add.class, Edit.class})
    private Integer status;

    @ApiModelProperty(value = "角色描述", position = 60)
    @Size(max = 200, message = "备注信息长度应在0-200之间", groups = {Add.class, Edit.class})
    private String description;

    @ApiModelProperty(value = "菜单权限", position = 70)
    private List<MenuDO> menuList;

    @ApiModelProperty(value = "角色已有菜单权限", position = 80)
    private List<Long> checkMenuList;
}