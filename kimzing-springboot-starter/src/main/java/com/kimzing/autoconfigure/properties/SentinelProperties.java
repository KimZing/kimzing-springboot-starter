package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 哨兵熔断降级配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/19 20:22
 */
@Data
@ConfigurationProperties(prefix = "kimzing.sentinel", ignoreUnknownFields = true)
public class SentinelProperties {

    /**
     * 是否开启Sentinel相关配置
     */
    private Boolean enabled;

    /**
     * 异常拦截配置
     */
    private ExceptionHandlerProperties exceptionHandler = new ExceptionHandlerProperties();

    @Data
    public static class ExceptionHandlerProperties {

        /**
         * 控制开关
         */
        private Boolean enabled;

        /**
         * 提示码
         */
        private String code = "SECURITY";

        /**
         * 提示信息
         */
        private String message = "请求已被安全管控系统拦截，请联系管理员!";

    }

}
