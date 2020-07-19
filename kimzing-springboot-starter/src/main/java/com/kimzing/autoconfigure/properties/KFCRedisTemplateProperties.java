package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring容器加载的属性文件.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/25 16:29
 */
@Data
@ConfigurationProperties(prefix = "kimzing.redis", ignoreUnknownFields = true)
public class KFCRedisTemplateProperties {

    /**
     * 是否开启自定义redis模板
     */
    private String enabled;

    /**
     * redis服务级别统一前缀
     */
    private String prefix;

    /**
     * redis序列化与反序列化的时间格式
     */
    private String timePattern = "yyyy-MM-dd HH:mm";

}
