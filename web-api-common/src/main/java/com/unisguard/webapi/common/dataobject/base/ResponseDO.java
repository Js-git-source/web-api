package com.unisguard.webapi.common.dataobject.base;

import com.unisguard.webapi.common.exception.MessageCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zemel
 * @date 2019/10/30 16:10
 */
@Getter
@Setter
@ApiModel(value = "ResponseDO", description = "响应")
public class ResponseDO<T> implements Serializable {

    @ApiModelProperty(value = "返回结果码", position = 1, required = true)
    private Integer code;

    @ApiModelProperty(value = "返回结果信息", position = 2, required = true)
    private String msg;

    @ApiModelProperty(value = "总条数", position = 3, required = true)
    private Long total;

    @ApiModelProperty(value = "返回数据体", position = 4, required = true)
    private T data;

    private ResponseDO() {
    }

    public static <T> ResponseDO<T> success() {
        return success(MessageCode.EXECUTE_SUCCESS, "ok");
    }

    public static <T> ResponseDO<T> success(T data) {
        return success(MessageCode.EXECUTE_SUCCESS, "ok", data);
    }

    public static <T> ResponseDO<T> success(T data, Long total) {
        return success(MessageCode.EXECUTE_SUCCESS, "ok", data, total);
    }

    public static <T> ResponseDO<T> success(Integer code, String msg) {
        return success(code, msg, null);
    }

    public static <T> ResponseDO<T> success(Integer code, String msg, T data) {
        return success(code, msg, data, 0L);
    }

    private static <T> ResponseDO<T> success(Integer code, String msg, T data, Long total) {
        ResponseDO<T> response = new ResponseDO<>();
        response.code = code;
        response.msg = msg;
        response.data = data;
        response.total = total;
        return response;
    }

    public static <T> ResponseDO<T> failure() {
        return failure(MessageCode.EXECUTE_FAILURE, "fail");
    }

    public static <T> ResponseDO<T> failure(String msg) {
        return failure(MessageCode.EXECUTE_FAILURE, msg);
    }

    public static <T> ResponseDO<T> failure(Integer code, String msg) {
        return failure(code, msg, null);
    }

    public static <T> ResponseDO<T> failure(Integer code, String msg, T data) {
        ResponseDO<T> response = new ResponseDO<>();
        response.code = code;
        response.msg = msg;
        response.data = data;
        return response;
    }
}
