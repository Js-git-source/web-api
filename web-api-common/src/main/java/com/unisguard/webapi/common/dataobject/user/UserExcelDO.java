package com.unisguard.webapi.common.dataobject.user;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.unisguard.webapi.common.annotation.JSONDict;
import com.unisguard.webapi.common.dataobject.base.BaseDO;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "用户")
@Validated
public class UserExcelDO extends BaseDO {

    private String name;

    @ColumnWidth(50)
    private String account;

    private Long deptId;

    private String title;

    private Integer type;

    private Long phone;

    private String email;

    private Integer status;

    private String description;

    private String confirmPassword;

}
