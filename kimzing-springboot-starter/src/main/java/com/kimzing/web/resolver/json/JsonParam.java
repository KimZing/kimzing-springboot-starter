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

    /**
     * 参数名称，和query param中的key对应，如果不存在则使用方法中的参数的变量名
     * @return
     */
    String name() default "";

    /**
     * 时间格式
     * @return
     */
    String pattern() default "yyyy-MM-dd HH:mm";

    /**
     * 该参数是否为必须
     * @return
     */
    boolean required() default true;
}