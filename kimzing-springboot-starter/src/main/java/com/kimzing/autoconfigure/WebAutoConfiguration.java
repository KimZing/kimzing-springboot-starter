package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.WebProperties;
import com.kimzing.web.advice.ExceptionAdvice;
import com.kimzing.web.info.KimZingInfoController;
import com.kimzing.web.resolver.MethodParamResolverConfiguration;
import com.kimzing.web.resolver.json.JsonParamResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate自动配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/26 10:51
 */
@Configuration
@EnableConfigurationProperties({WebProperties.class})
public class WebAutoConfiguration {

    /**
     * 注入RestTemplate实例，用于Http接口调用
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    @ConditionalOnProperty(prefix = "kimzing.web.restTemplate",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }


    /**
     * 信息接口
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.info",
            name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean(KimZingInfoController.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
    public KimZingInfoController infoController(WebProperties webProperties) {
        KimZingInfoController kimZingInfoController = new KimZingInfoController();
        kimZingInfoController.setInfoMap(webProperties.getInfo().getParams());
        return kimZingInfoController;
    }

    /**
     * 异常切面拦截处理
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.advice",
            name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean(ExceptionAdvice.class)
    public ExceptionAdvice exceptionAdvice() {
        return new ExceptionAdvice();
    }

    /**
     * json参数解析器
     *
     * @return
     */
    @Bean
    public JsonParamResolver jsonParamResolver() {
        return new JsonParamResolver();
    }

    /**
     * json参数解析器配置
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.resolver.json.enabled",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public MethodParamResolverConfiguration methodParamResolverConfiguration(ApplicationContext context) {
        return new MethodParamResolverConfiguration(context);
    }

}
