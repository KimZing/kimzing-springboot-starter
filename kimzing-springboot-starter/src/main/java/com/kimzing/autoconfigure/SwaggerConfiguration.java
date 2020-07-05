package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.SwaggerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/5 16:51
 */
@Configuration
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(prefix = "kimzing.swagger", name = "enabled", havingValue = "true")
@ConditionalOnClass({Docket.class})
public class SwaggerConfiguration {

    @Bean
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
