package com.unisguard.webapi.common.dataobject.api;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @description api管理服务端实体类
 * @author sunsaike
 */
@Data
@ApiModel(value = "系统API")
public class ApiDO extends BaseDO {
    @ApiModelProperty(value = "接口名称", position = 10)
    @NotBlank(message = "接口名称不能为空", groups = {Add.class, Edit.class})
    @Length(max = 20,message = "接口名称超长",groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "接口URL", position = 20)
    @NotBlank(message = "接口URL不能为空", groups = {Add.class, Edit.class})
    @Length(max = 2083,message = "接口URL超长",groups = {Add.class, Edit.class})
    private String url;

    @ApiModelProperty(value = "接口描述", position = 30)
    @Length(max = 200,message = "接口描述超长",groups = {Add.class, Edit.class})
    private String description;
}