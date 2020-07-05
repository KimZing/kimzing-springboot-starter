package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * KnifeSwagger属性配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/5 16:51
 */
@Data
@ConfigurationProperties(prefix = "kimzing.swagger", ignoreUnknownFields = true)
public class SwaggerProperties {


    /**
     * 控制开关
     */
    private Boolean enabled;

    /**
     * 标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;

    /**
     * 项目路径
     */
    private String termsOfServiceUrl;

    /**
     * 作者
     */
    private String authorName;

    /**
     * 邮箱
     */
    private String authorEmail;

    /**
     * 作者主页
     */
    private String authorUrl;

    /**
     * 版本
     */
    private String version;

    /**
     * 扫描的路径
     */
    private String basePackage;

}
