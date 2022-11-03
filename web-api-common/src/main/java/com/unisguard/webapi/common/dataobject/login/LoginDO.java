package com.unisguard.webapi.common.dataobject.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zemel
 * @date 2022/1/9 19:35
 */
@Setter
@Getter
@ApiModel(value = "登录")
public class LoginDO implements Serializable {
    @ApiModelProperty(value = "账号", position = 10)
    private String account;

    @ApiModelProperty(value = "密码", position = 20)
    private String password;

    @ApiModelProperty(value = "验证码", position = 30)
    private String authCode;
}
