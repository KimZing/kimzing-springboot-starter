package com.kimzing.autoconfigure.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * KnifeSwagger属性配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/5 16:51
 */
@Data
@ConfigurationProperties(prefix = "kimzing.swagger", ignoreUnknownFields = true)
public class SwaggerProperties {

    @Autowired
    @JsonIgnore
    private Environment environment;

    @PostConstruct
    public void init() {
        this.overrideFromEnv();
    }

    private void overrideFromEnv() {
        if (StringUtils.isEmpty(this.getTitle())) {
            this.setTitle(environment.resolvePlaceholders("${spring.application.name:}"));
        }
        if (StringUtils.isEmpty(this.getTermsOfServiceUrl())) {
            this.setTermsOfServiceUrl("http://localhost:" + environment.resolvePlaceholders("${server.port:8080}"));
        }
    }

    /**
     * 控制开关
     */
    private Boolean enabled;

    /**
     * 标题
     */
    private String title;

    /**
     * 项目描述
     */
    private String description = "这个人很懒，没有填写描述信息";

    /**
     * 项目路径
     */
    private String termsOfServiceUrl;

    /**
     * 作者
     */
    private String authorName = "KimZing";

    /**
     * 邮箱
     */
    private String authorEmail = "kimzing@163.com";

    /**
     * 作者主页
     */
    private String authorUrl = "http://kimzing.com";

    /**
     * 版本
     */
    private String version = "1.0.0";

    /**
     * 扫描的路径
     */
    private String basePackage;

}
