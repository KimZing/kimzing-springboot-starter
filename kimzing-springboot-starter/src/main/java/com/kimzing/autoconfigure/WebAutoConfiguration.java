package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.WebProperties;
import com.kimzing.web.advice.ExceptionAdvice;
import com.kimzing.web.advice.ResultAdvice;
import com.kimzing.web.info.KimZingInfoController;
import com.kimzing.web.log.WebRequestLogAspect;
import com.kimzing.web.resolver.MethodParamResolverConfiguration;
import com.kimzing.web.resolver.json.JsonParamResolver;
import io.undertow.UndertowOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

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
     * 将Converter的顺序进行调整，否则全局结果处理在处理字符串返回时会报类型转换错误，因为会经过StringConverter
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(0, new MappingJackson2HttpMessageConverter());
            }
        };
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
     * 开启URL和cookie转义字符功能
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.escape.enabled",
            name = "enabled", havingValue = "true", matchIfMissing = false)
    public WebServerFactoryCustomizer webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<UndertowServletWebServerFactory>() {
            @Override
            public void customize(UndertowServletWebServerFactory factory) {
                //url配置
                factory.addBuilderCustomizers(builder ->
                        builder.setServerOption(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, Boolean.TRUE));
                //cookie配置
                factory.addBuilderCustomizers(builder ->
                        builder.setServerOption(UndertowOptions.ALLOW_EQUALS_IN_COOKIE_VALUE, Boolean.TRUE));
            }
        };
    }

    /**
     * SpringMVC跨域支持
     * @param webProperties
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "kimzing.web.cors",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnClass(WebMvcConfigurer.class)
    public CorsFilter corsConfigurer(WebProperties webProperties) {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        //是否允许发送Cookie信息
        corsConfiguration.setAllowCredentials(true);
        //放行哪些原始域
        Arrays.stream(webProperties.getCors().getOrigins())
                .forEach(o -> corsConfiguration.addAllowedOrigin(o));
        corsConfiguration.addAllowedHeader("*");
        //放行哪些原始域(请求方式)
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
