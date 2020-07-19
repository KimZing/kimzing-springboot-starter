package com.kimzing.log;

import java.lang.annotation.*;

/**
 * 日志打印.
 * <p>
 * 标有该注解的方法，将会打印其相应参数
 * </p>
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/24 15:07
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogIgnore {

}
