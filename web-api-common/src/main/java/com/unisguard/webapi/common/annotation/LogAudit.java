package com.unisguard.webapi.common.annotation;

import com.unisguard.webapi.common.constant.DictConstant;

import java.lang.annotation.*;

/**
 * 操作日志记录-审计日志
 *
 * @author wanglinghui
 * @Date 2022-01-13
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAudit {

    /**
     * 服务/引擎编号 默认是web服务
     *
     * @return
     */
    int engineId() default DictConstant.ENGINE_TYPE_WEB;

    /**
     * 模块编号
     */
    int moduleId();

    /**
     * 操作类型编号
     */
    int opType();


}
