package com.unisguard.webapi.common.exception;

/**
 * 平台消息码字典
 */
public class MessageCode {
    // ---------------------------系统消息---------------------------
    // 处理成功
    public static final Integer EXECUTE_SUCCESS = 0;

    // 处理失败
    public static final Integer EXECUTE_FAILURE = -1;

    // 未授权
    public static final Integer UNLICENSED = -2;

    // 登录过期
    public static final Integer LOGIN_EXPIRE = -3;
}
