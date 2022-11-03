package com.unisguard.webapi.common.dataobject.dept;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "部门关系人")
public class DeptUserDO extends BaseDO {
    @ApiModelProperty(value = "部门编号", position = 10)
    @NotNull(message = "部门编号不能为空", groups = {Add.class, Edit.class})
    private Long deptId;

    @ApiModelProperty(value = "用户编号", position = 20)
    @NotNull(message = "用户编号不能为空", groups = {Add.class, Edit.class})
    private Long userId;

    @ApiModelProperty(value = "用户类型(1000100)", position = 30)
    @NotNull(message = "用户类型(1000100)不能为空", groups = {Add.class, Edit.class})
    private Integer userType;

    @ApiModelProperty(value = "用户名称", position = 40)
    private String userName;
}