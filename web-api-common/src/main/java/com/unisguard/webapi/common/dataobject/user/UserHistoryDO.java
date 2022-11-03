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
@ApiModel(value = "账号历史配置信息")
public class UserHistoryDO extends BaseDO {
    @ApiModelProperty(value = "用户编号", position = 10)
    @NotNull(message = "用户编号不能为空", groups = {Add.class, Edit.class})
    private Long userId;

    @ApiModelProperty(value = "账号密码", position = 20)
    @NotBlank(message = "账号密码不能为空", groups = {Add.class, Edit.class})
    private String passwrod;
}