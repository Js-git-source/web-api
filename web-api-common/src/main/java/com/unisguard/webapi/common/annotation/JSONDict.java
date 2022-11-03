package com.unisguard.webapi.common.annotation;

import java.lang.annotation.*;

/**
 * @author zemel
 * @date 2022/1/5 10:42
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSONDict {
    int codeType() default 0;
}
