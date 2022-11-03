package com.unisguard.webapi.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zemel
 * @date 2021/12/25 20:35
 */
@Getter
@Setter
public class ApplicationException extends RuntimeException {
    private int code;

    private Object data;

    private Throwable innerException;

    public ApplicationException(String message) {
        super(message);
        this.code = MessageCode.EXECUTE_FAILURE;
    }

    public ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public ApplicationException(String message, Throwable throwable) {
        super(throwable);
    }
}
