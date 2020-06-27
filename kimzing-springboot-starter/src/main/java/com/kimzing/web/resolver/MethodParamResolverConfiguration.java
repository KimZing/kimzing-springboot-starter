package com.kimzing.web.resolver;

import org.springframework.context.ApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

/**
 * 方法参数解析器配置加载.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/31 13:01
 */
public class MethodParamResolverConfiguration implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public MethodParamResolverConfiguration(ApplicationContext context) {
        this.applicationContext = context;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        Map<String, HandlerMethodArgumentResolver> beans =
                applicationContext.getBeansOfType(HandlerMethodArgumentResolver.class);

        beans.forEach((name, bean) -> resolvers.add(bean));
    }
}
