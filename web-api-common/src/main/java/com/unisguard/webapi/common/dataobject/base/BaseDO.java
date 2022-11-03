package com.unisguard.webapi.common.dataobject.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zemel
 * @date 2021/12/13 15:32
 */
@Setter
@Getter
public class BaseDO implements Serializable {
    @ApiModelProperty(value = "数据编号", position = 1)
    @NotNull(message = "数据编号不能为空", groups = {ID.class, Edit.class})
    private Long id;

    @ApiModelProperty(value = "修改人编号", position = 5000)
    private Long updateUserId;

    @ApiModelProperty(value = "修改人名称", position = 5000)
    private String updateUserName;

    @ApiModelProperty(value = "修改时间", position = 6000)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "分页页数", position = 7000, example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数量", position = 8000, example = "10")
    private Integer limit = 10;

    public interface Add {
    }

    public interface Edit {
    }

    public interface ID {
    }
}
