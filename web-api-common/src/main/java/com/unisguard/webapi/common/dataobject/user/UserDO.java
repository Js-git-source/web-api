package com.unisguard.webapi.common.dataobject.user;

import com.unisguard.webapi.common.annotation.JSONDict;
import com.unisguard.webapi.common.dataobject.base.BaseDO;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "用户")
@Validated
public class UserDO extends BaseDO {
    @ApiModelProperty(value = "姓名", position = 10)
    @NotBlank(message = "姓名不能为空", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 20, message = "用户姓名长度应在1-20之间", groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "账号", position = 20)
    @NotBlank(message = "账号不能为空", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 20, message = "用户账号长度应在1-20之间", groups = {Add.class, Edit.class})
    private String account;

    @ApiModelProperty(value = "密码", position = 30)
    @NotBlank(message = "密码不能为空", groups = {Add.class})
    private String password;

    @ApiModelProperty(value = "部门编号", position = 40)
    @NotNull(message = "部门编号不能为空", groups = {Add.class, Edit.class})
    private Long deptId;

    @ApiModelProperty(value = "职务", position = 50)
    @Size(max = 20, message = "职务长度应在0-20之间", groups = {Add.class, Edit.class})
    private String title;

    @JSONDict(codeType = 1000300)
    @ApiModelProperty(value = "用户类型(1000300)", position = 60)
    @NotNull(message = "用户类型(1000300)不能为空", groups = {Add.class, Edit.class})
    private Integer type;

    @ApiModelProperty(value = "手机号码", position = 70)
    private Long phone;

    @ApiModelProperty(value = "邮箱地址", position = 80)
    private String email;

    @ApiModelProperty(value = "数据状态", position = 90)
    @NotNull(message = "数据状态不能为空", groups = {Add.class, Edit.class})
    private Integer status;

    @ApiModelProperty(value = "登录失败次数", position = 100)
    private Integer failureCount;

    @ApiModelProperty(value = "首次登录失败时间", position = 110)
    private LocalDateTime failureFirstTime;

    @ApiModelProperty(value = "登录失败锁定时间", position = 120)
    private LocalDateTime failureFinishTime;

    @ApiModelProperty(value = "账号描述", position = 130)
    @Size(max = 200, message = "备注信息长度应在0-200之间", groups = {Add.class, Edit.class})
    private String description;

    @ApiModelProperty(value = "确认密码", position = 140)
    @NotBlank(message = "确认密码不能为空", groups = {Add.class})
    private String confirmPassword;

    @ApiModelProperty(value = "角色集合", position = 150)
    private List<UserRoleDO> userRoleList;

    @ApiModelProperty(value = "用户组集合", position = 160)
    private List<GroupUserDO> groupUserList;

    @ApiModelProperty(value = "菜单集合", position = 170)
    private List<MenuDO> menuList;

    @ApiModelProperty(value = "用户所属部门及其子部门", position = 180)
    private List<Long> deptIdList;

    private String deptName;

    @ApiModelProperty(value = "首次登录", position = 190)
    private Integer firstLogin;
}
