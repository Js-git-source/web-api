package com.unisguard.webapi.common.dataobject.user;

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
@ApiModel(value = "查询关联用户参数")
public class UserParamDO extends BaseDO {
   @ApiModelProperty(value = "角色", position = 10)
   private Long roleId;
   @ApiModelProperty(value = "名称或账号", position = 20)
   private String nameOrAccount;
   @ApiModelProperty(value = "部门或职务", position = 30)
   private String deptOrTitle;
   @ApiModelProperty(value = "用户组", position = 40)
   private Long groupId;
   @ApiModelProperty(value = "部门", position = 50)
   private Long deptId;
   @ApiModelProperty(value = "职务", position = 60)
   private String title;
   @ApiModelProperty(value = "部门集合", position = 70)
   private List<Long> deptIdList;
   @ApiModelProperty(value = "用户集合", position = 80)
   private List<Long> userIdList;
   @ApiModelProperty(value = "用户id字符串", position = 80)
   private String userIds;
}
