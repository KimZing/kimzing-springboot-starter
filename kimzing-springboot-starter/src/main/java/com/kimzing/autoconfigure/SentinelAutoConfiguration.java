package com.kimzing.autoconfigure;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.kimzing.autoconfigure.properties.SentinelProperties;
import com.kimzing.sentinel.SentinelBlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 哨兵熔断降级配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/19 20:21
 */
@Configuration
@ConditionalOnClass(SentinelResource.class)
@EnableConfigurationProperties({SentinelProperties.class})
@ConditionalOnProperty(prefix = "kimzing.sentinel", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SentinelAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "kimzing.sentinel.exceptionHandler", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    public SentinelBlockExceptionHandler sentinelBlockExceptionHandler(SentinelProperties sentinelProperties) {
        return new SentinelBlockExceptionHandler(sentinelProperties.getExceptionHandler().getCode(),
                sentinelProperties.getExceptionHandler().getMessage());
    }

}
