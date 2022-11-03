package com.unisguard.webapi.common.dataobject.menu;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ApiModel(value = "菜单")
public class MenuDO extends BaseDO {
    @ApiModelProperty(value = "菜单名称", position = 10)
    @NotBlank(message = "菜单名称不能为空", groups = {Add.class, Edit.class})
    @Pattern(regexp = "^*(?!-)(?!.*-$)[\u4e00-\u9fa5a-zA-Z-]((?!--).)*+$", message = "菜单名称限制为中文或英文字母以及中横线(不可连续，不可首尾) 长度1-20", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 20, message = "菜单名称限制为中文或英文字母以及中横线(不可连续，不可首尾) 长度1-20", groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "菜单类型(1000200)", position = 20)
    @NotNull(message = "菜单类型(1000200)不能为空", groups = {Add.class, Edit.class})
    private Integer type;

    @ApiModelProperty(value = "菜单标识", position = 30)
    @NotBlank(message = "菜单标识不能为空", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 100, message = "菜单标识限制为英文字符，不包含空格(长度1-100)", groups = {Add.class, Edit.class})
    @Pattern(regexp = "^[A-Za-z]+$", message = "菜单标识限制为英文字符，不包含空格(长度1-100)", groups = {Add.class, Edit.class})
    private String tag;

    @ApiModelProperty(value = "排序", position = 40)
    @NotNull(message = "显示顺序不能为空", groups = {Add.class, Edit.class})
    @Range(min = 1, max = 9999, message = "显示顺序限制为数字(长度1-4)", groups = {Add.class, Edit.class})
    private Integer sort;

    @ApiModelProperty(value = "请求地址", position = 50)
    @Size(max = 2083, message = "请求地址限制为英文字母以及'/'('/'开头，不可连续，不可'/'结尾) 长度1-2083", groups = {Add.class, Edit.class})
    @Pattern(regexp = "^*(?!//)(?!.*/$)/[a-zA-Z/]((?!//).)*+$", message = "请求地址限制为英文字母以及'/'('/'开头，不可连续，不可'/'结尾) 长度1-2083", groups = {Add.class, Edit.class})
    private String url;

    @ApiModelProperty(value = "显示图标", position = 60)
    private String icon;

    @ApiModelProperty(value = "新窗口(1000000)", position = 70)
    private Integer openWin;

    @ApiModelProperty(value = "数据状态", position = 80)
    private Integer status;

    @ApiModelProperty(value = "层级", position = 90)
    private Integer level;

    @ApiModelProperty(value = "上级菜单编号", position = 100)
    private Long parentId;

    @ApiModelProperty(value = "上级菜单名称", position = 100)
    private String parentName;

    @ApiModelProperty(value = "下级菜单", position = 100)
    private List<MenuDO> children;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDO menuDO = (MenuDO) o;
        return Objects.equals(name, menuDO.name) && Objects.equals(parentId, menuDO.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentId);
    }
}
