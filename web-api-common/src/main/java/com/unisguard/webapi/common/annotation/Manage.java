package com.unisguard.webapi.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author zemel
 * @date 2021/12/8 9:37
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Manage {
    String value() default "";
}
