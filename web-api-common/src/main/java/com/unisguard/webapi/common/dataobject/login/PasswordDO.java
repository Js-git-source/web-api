package com.unisguard.webapi.common.dataobject.login;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author wangzemo
 * @date 2022/1/21 9:49
 */
@Setter
@Getter
@ApiModel(value = "密码")
public class PasswordDO implements Serializable {

    @ApiModelProperty(value = "旧密码", position = 10)
    @NotBlank(message = "旧密码不能为空", groups = BaseDO.Edit.class)
    private String oldPassword;

    @ApiModelProperty(value = "新密码", position = 20)
    @NotBlank(message = "新密码不能为空", groups = BaseDO.Edit.class)
    private String newPassword;

    @ApiModelProperty(value = "确认密码", position = 30)
    @NotBlank(message = "确认密码不能为空", groups = BaseDO.Edit.class)
    private String confirmPassword;
}
