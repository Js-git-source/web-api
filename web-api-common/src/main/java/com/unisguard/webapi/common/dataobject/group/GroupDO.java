package com.unisguard.webapi.common.dataobject.group;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import com.unisguard.webapi.common.dataobject.role.RoleDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
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
@ApiModel(value = "用户组")
public class GroupDO extends BaseDO {
    @ApiModelProperty(value = "组名称", position = 10)
    @NotBlank(message = "组名称不能为空", groups = {Add.class, Edit.class})
    @Pattern(regexp = "^[A-Za-z\u4e00-\u9fa5]+$", message = "用户组名称只允许中文或者大小写英文字母", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 20, message = "用户组名称长度应在1-20之间", groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "上级编号", position = 20)
    private Long pid;

    @ApiModelProperty(value = "负责人账号编号", position = 30)
    @NotNull(message = "负责人账号编号不能为空", groups = {Add.class, Edit.class})
    private Long mgrId;

    @ApiModelProperty(value = "账号状态", position = 40)
    @NotNull(message = "账号状态不能为空", groups = {Add.class, Edit.class})
    private Integer status;

    @ApiModelProperty(value = "用户组描述", position = 50)
    @Size(max = 500, message = "描述长度应在0-500之间", groups = {Add.class, Edit.class})
    private String description;

    @ApiModelProperty(value = "角色集合", position = 60)
    private List<GroupRoleDO> groupRoleList;

    @ApiModelProperty(value = "用户集合", position = 70)
    private List<GroupUserDO> groupUserList;

    @ApiModelProperty(value = "上级名称", position = 80)
    private String pname;

    @ApiModelProperty(value = "子用户组集合", position = 90)
    private List<GroupDO> children;
}
