package com.unisguard.webapi.common.dataobject.user;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "密码组成")
public class PasswordCompositoinDo extends BaseDO {

    private String number;

    private String lowercase;

    private String uppercase;

    private String special;

    private String special_example;
}