package com.unisguard.webapi.common.dataobject.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * @author zemel
 * @date 2022/1/10 9:38
 */
@Setter
@Getter
@ApiModel(value = "当前用户")
public class CurrentUserDO implements Serializable {
    @ApiModelProperty(value = "数据编号", position = 1)
    private Long id;

    @ApiModelProperty(value = "姓名", position = 10)
    private String name;

    @ApiModelProperty(value = "账号", position = 20)
    private String account;

    @ApiModelProperty(value = "部门编号", position = 40)
    private Long deptId;

    @ApiModelProperty(value = "部门名称", position = 50)
    private String deptName;

    @ApiModelProperty(value = "职务", position = 60)
    private String title;

    @ApiModelProperty(value = "用户类型", position = 70)
    private Integer type;

    @ApiModelProperty(value = "用户类型名称", position = 80)
    private Integer typeName;

    @ApiModelProperty(value = "手机号码", position = 90)
    private Long phone;

    @ApiModelProperty(value = "邮箱地址", position = 100)
    private String email;

    @ApiModelProperty(value = "数据状态", position = 110)
    private Integer status;

    @ApiModelProperty(value = "数据状态名称", position = 120)
    private Integer statusName;

    @ApiModelProperty(value = "账号描述", position = 130)
    private String description;

    @ApiModelProperty(value = "权限集合", position = 140)
    private Set<String> urlSet;

    @ApiModelProperty(value = "token", position = 150)
    private String token;

    @ApiModelProperty(value = "菜单集合", position = 160)
    private Set<String> menuUrlSet;

    @ApiModelProperty(value = "角色集合", position = 170)
    private Set<String> roleNameSet;

    @ApiModelProperty(value = "首次登录强制修改密码", position = 180)
    private boolean firstForce = false;
}
