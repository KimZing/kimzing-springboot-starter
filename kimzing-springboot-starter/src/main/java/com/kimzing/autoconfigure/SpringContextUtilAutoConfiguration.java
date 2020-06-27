package com.kimzing.autoconfigure;

import com.kimzing.utils.spring.SpringContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringContext容器工具自动配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/26 10:55
 */
@Configuration
@ConditionalOnClass(ApplicationContext.class)
public class SpringContextUtilAutoConfiguration {

    /**
     * SpringContext容器工具
     *
     * @return
     */
    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

}
