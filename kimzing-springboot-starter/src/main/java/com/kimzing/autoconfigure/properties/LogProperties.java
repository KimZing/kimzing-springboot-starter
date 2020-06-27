package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志相关配置
 */
@Data
@ConfigurationProperties(prefix = "kimzing.log", ignoreUnknownFields = true)
public class LogProperties {

    /**
     * 日志时间的格式，默认yyyy-MM-dd HH:mm:ss:SSS
     */
    private String timePattern;

    /**
     * 控制日志开关
     */
    private Boolean enabled;

}