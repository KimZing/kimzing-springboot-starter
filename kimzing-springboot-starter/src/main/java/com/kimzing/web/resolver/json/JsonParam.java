package com.kimzing.web.resolver.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Json类型请求参数.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/30 22:49
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonParam {
    boolean required() default true;
}