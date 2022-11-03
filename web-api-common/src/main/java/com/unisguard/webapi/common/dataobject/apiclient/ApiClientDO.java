package com.unisguard.webapi.common.dataobject.apiclient;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @description api管理-客户端实体
 * @author sunsaike
 */
@Data
@ApiModel(value = "API客户端")
public class ApiClientDO extends BaseDO {
    @ApiModelProperty(value = "客户端名称", position = 10)
    @Length(max = 20,message = "客户端名称超长",groups = {Add.class, Edit.class})
    @NotBlank(message = "客户端名称不能为空", groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "客户端APPID", position = 20)
    @Length(max = 200,message = "客户端APPID超长",groups = {Add.class, Edit.class})
    private String appid;

    @ApiModelProperty(value = "客户端APPKey", position = 30)
    @NotBlank(message = "客户端APPKey不能为空", groups = {Edit.class})
    @Length(max = 200,message = "客户端APPKey超长",groups = {Add.class, Edit.class})
    private String appkey;

    @ApiModelProperty(value = "数据状态", position = 40)
    @NotNull(message = "数据状态不能为空", groups = {Add.class, Edit.class})
    private Integer status;

    @ApiModelProperty(value = "客户端描述", position = 70)
    @Length(max = 200,message = "客户端描述超长",groups = {Add.class, Edit.class})
    private String description;

    @ApiModelProperty(value = "授权接口", position = 80)
    private Integer apiCount;
}