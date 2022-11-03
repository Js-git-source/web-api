package com.unisguard.webapi.common.dataobject.dict;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ApiModel(value = "系统字典")
public class DictDO extends BaseDO {
    @ApiModelProperty(value = "分类编号", position = 10)
    @NotNull(message = "分类编号不能为空", groups = {Add.class, Edit.class})
    private Integer codeType;

    @ApiModelProperty(value = "字典编号", position = 20)
    @NotNull(message = "字典编号不能为空", groups = {Add.class, Edit.class})
    private Integer code;

    @ApiModelProperty(value = "字典名称", position = 30)
    @NotBlank(message = "字典名称不能为空", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 20, message = "字典名称长度应在1-20之间", groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "字典顺序", position = 40)
    @NotNull(message = "字典顺序不能为空", groups = {Add.class, Edit.class})
    private Integer level;

    @ApiModelProperty(value = "数据描述", position = 50)
    @Size(min = 0, max = 200, message = "数据描述长度应在1-200之间", groups = {Add.class, Edit.class})
    private String description;

    private String code_type_name;
}