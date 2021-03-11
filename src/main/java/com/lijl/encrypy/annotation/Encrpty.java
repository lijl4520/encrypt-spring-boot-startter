package com.lijl.encrypy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Lijl
 * @ClassName Encrpty
 * @Description 加密注解
 * @Date 2021/3/10 9:41
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Encrpty {
}
