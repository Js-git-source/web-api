package com.unisguard.webapi.common.dataobject.weakpass;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "弱口令字典")
public class WeakPassDO extends BaseDO {
    @ApiModelProperty(value = "密码(明文)", position = 10)
    @NotBlank(message = "密码(明文)不能为空", groups = {Add.class, Edit.class})
    private String pass;
}