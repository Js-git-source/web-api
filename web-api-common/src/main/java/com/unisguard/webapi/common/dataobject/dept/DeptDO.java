package com.unisguard.webapi.common.dataobject.dept;

import com.unisguard.webapi.common.dataobject.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "部门")
public class DeptDO extends BaseDO {
    @ApiModelProperty(value = "部门名称", position = 10)
    @NotBlank(message = "部门名称不能为空", groups = {Add.class, Edit.class})
    @Size(min = 1, max = 20, message = "部门名称长度应在1-20之间", groups = {Add.class, Edit.class})
    @Pattern(regexp = "^[A-Za-z\u4e00-\u9fa5]+$", message = "名称只允许中文或者大小写英文字母", groups = {Add.class, Edit.class})
    private String name;

    @ApiModelProperty(value = "上级编号", position = 20)
    @NotNull(message = "上级编号不能为空", groups = {Add.class, Edit.class})
    @Min(value = -1L, message = "父节点最小为-1", groups = {Add.class, Edit.class})
    private Long pid;

    @ApiModelProperty(value = "部门全路径", position = 30)
    private String path;

    @ApiModelProperty(value = "数据状态", position = 40)
    @NotNull(message = "数据状态不能为空", groups = {Add.class, Edit.class})
    private Integer status;

    @ApiModelProperty(value = "排序", position = 50)
    @NotNull(message = "排序不能为空", groups = {Add.class, Edit.class})
    @Range(min = 1, max = 9999, message = "排序只能在1-9999之间", groups = {Add.class, Edit.class})
    private Integer sort;

    @ApiModelProperty(value = "负责人列表", position = 60)
    private List<DeptUserDO> deptHeadList;

    @ApiModelProperty(value = "安全接口人列表", position = 70)
    private List<DeptUserDO> safetyInterfaceList;

    @ApiModelProperty(value = "更新时间", position = 80)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新者名称", position = 90)
    private String updateUserName;

    @ApiModelProperty(value = "上级部门名称", position = 100)
    private String pname;

    @ApiModelProperty(value = "子节点", position = 110)
    private List<DeptDO> children;

    @ApiModelProperty(value = "用户数量", position = 120)
    private Integer count = 0;
}