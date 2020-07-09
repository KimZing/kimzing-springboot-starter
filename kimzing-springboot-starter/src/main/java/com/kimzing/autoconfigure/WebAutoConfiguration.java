package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.WebProperties;
import com.kimzing.web.advice.ExceptionAdvice;
import com.kimzing.web.advice.ResultAdvice;
import com.kimzing.web.info.KimZingInfoController;
import com.kimzing.web.log.WebRequestLogAspect;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
     * 统一结果处理器.
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.result",
            name = "enabled", havingValue = "true")
    public ResultAdvice resultAdvice() {
        return new ResultAdvice();
    }

    /**
     * 异常切面拦截处理
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.advice",
            name = "enabled", havingValue = "true")
    public ExceptionAdvice exceptionAdvice() {
        return new ExceptionAdvice();
    }

    /**
     * 请求日志打印
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.log",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public WebRequestLogAspect logAdvice() {
        return new WebRequestLogAspect();
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

    /**
     * SpringMVC跨域支持
     * @param webProperties
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.cors",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(WebMvcConfigurer.class)
    public WebMvcConfigurer corsConfigurer(WebProperties webProperties) {
        return new WebMvcConfigurer() {
            @Override
            //重写父类提供的跨域请求处理的接口
            public void addCorsMappings(CorsRegistry registry) {
                //添加映射路径
                registry.addMapping("/**")
                        //放行哪些原始域
                        .allowedOrigins(webProperties.getCors().getOrigins())
                        //是否发送Cookie信息
                        .allowCredentials(true)
                        //放行哪些原始域(请求方式)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .maxAge(3600)
                        //放行哪些原始域(头部信息)
                        .allowedHeaders("*");
            }
        };
    }

}
