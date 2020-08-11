package com.kimzing.autoconfigure;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.kimzing.autoconfigure.properties.SwaggerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/5 16:51
 */
@EnableKnife4j
@EnableSwagger2
@Configuration
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(prefix = "kimzing.swagger", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({Docket.class,EnableKnife4j.class})
public class SwaggerConfiguration {

    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket docket(SwaggerProperties swaggerProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title(swaggerProperties.getTitle())
                                .description(swaggerProperties.getDescription())
                                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                                .contact(new Contact(swaggerProperties.getAuthorName(),
                                        swaggerProperties.getAuthorUrl(),
                                        swaggerProperties.getAuthorEmail()))
                                .version(swaggerProperties.getVersion())
                                .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }


}
