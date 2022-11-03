package com.unisguard.webapi.common.dataobject.lic;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "系统授权配置")
public class LicDO extends BaseDO {
    @ApiModelProperty(value = "授权时间", position = 10)
    private LocalDateTime authTime;

    @ApiModelProperty(value = "授权客户", position = 20)
    @NotBlank(message = "授权客户不能为空", groups = {Add.class, Edit.class})
    private String authName;

    @ApiModelProperty(value = "授权周期", position = 30)
    @NotNull(message = "授权周期不能为空", groups = {Add.class, Edit.class})
    private Integer authCycle;

    @ApiModelProperty(value = "授权内容", position = 20)
    @NotBlank(message = "授权内容不能为空", groups = {Add.class, Edit.class})
    private String authContent;

    @ApiModelProperty(value = "授权密钥", position = 20)
    @NotBlank(message = "授权密钥不能为空", groups = {Add.class, Edit.class})
    private String secretKey;

    @ApiModelProperty(value = "经度", position = 40)
    @NotBlank(message = "经度不能为空", groups = {Add.class, Edit.class})
    private String longitude;

    @ApiModelProperty(value = "纬度", position = 50)
    @NotBlank(message = "纬度不能为空", groups = {Add.class, Edit.class})
    private String latitude;

    @ApiModelProperty(value = "国家(中文)", position = 60)
    @NotBlank(message = "国家(中文)不能为空", groups = {Add.class, Edit.class})
    private String country;

    @ApiModelProperty(value = "国家(国家简码)", position = 70)
    @NotBlank(message = "国家(国家简码)不能为空", groups = {Add.class, Edit.class})
    private String countryCode;

    @ApiModelProperty(value = "区域/省(中文)", position = 80)
    @NotBlank(message = "区域/省(中文)不能为空", groups = {Add.class, Edit.class})
    private String area;

    @ApiModelProperty(value = "城市(中文)", position = 90)
    @NotBlank(message = "城市(中文)不能为空", groups = {Add.class, Edit.class})
    private String city;

    @ApiModelProperty(value = "数据状态", position = 100)
    @NotNull(message = "数据状态不能为空", groups = {Add.class, Edit.class})
    private Integer status;

    @ApiModelProperty(value = "最后修改密码时间", position = 120)
    private LocalDateTime lastPwdTime;

    @ApiModelProperty(value = "剩余天数", position = 130)
    private Integer remainDay;
}