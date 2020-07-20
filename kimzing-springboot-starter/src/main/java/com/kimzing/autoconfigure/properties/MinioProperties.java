package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Minio配置文件.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/20 01:11
 */
@Data
@ConfigurationProperties(prefix = "kimzing.minio", ignoreUnknownFields = true)
public class MinioProperties {

    /**
     * 是否开启minio注入
     */
    private Boolean enabled;

    /**
     * minio服务地址
     */
    private String url;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 统一前缀，与prefixType互斥，prefix级别优先
     */
    private String prefix;

    /**
     * 前缀类型: 目前仅支持time
     */
    private String prefixType;

    /**
     * 时间格式化类型
     */
    private String timePattern;

}
