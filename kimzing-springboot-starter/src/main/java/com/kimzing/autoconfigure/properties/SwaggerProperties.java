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
    private String title = "${spring.application.name}";

    /**
     * 项目描述
     */
    private String description = "项目描述";

    /**
     * 项目路径
     */
    private String termsOfServiceUrl = "http://localhost:8080";

    /**
     * 作者
     */
    private String authorName = "unknow";

    /**
     * 邮箱
     */
    private String authorEmail = "unknow";

    /**
     * 作者主页
     */
    private String authorUrl = "unkonw";

    /**
     * 版本
     */
    private String version = "1.0.0";

    /**
     * 扫描的路径
     */
    private String basePackage;

}
