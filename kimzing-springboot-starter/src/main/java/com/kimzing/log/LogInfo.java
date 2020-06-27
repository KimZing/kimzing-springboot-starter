package com.kimzing.log;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 日志信息载体.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/24 15:13
 */
@Data
@Builder
public class LogInfo {

    /**
     * 切入类名
     */
    private String className;

    /**
     * 切人的方法名
     */
    private String methodName;

    /**
     * 方法描述
     */
    private String desc;

    /**
     * 方法参数
     */
    private Map<String, Object> params;

    /**
     * 方法返回值
     */
    private Object result;

    /**
     * 异常对象
     */
    private Throwable throwable;

    /**
     * 方法执行开始时间
     */
    private String startTime;

    /**
     * 方法执行结束时间
     */
    private String endTime;

    /**
     * 方法运行时间，毫秒
     */
    private Long elapsedTimeInMilliseconds;

}
