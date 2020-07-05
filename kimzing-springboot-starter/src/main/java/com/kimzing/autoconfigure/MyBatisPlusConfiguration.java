package com.kimzing.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.kimzing.autoconfigure.properties.MyBatisPlusProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/5 16:40
 */
@Configuration
@EnableConfigurationProperties({MyBatisPlusProperties.class})
@ConditionalOnProperty(prefix = "kimzing.mybatis-plus", name = "enabled", havingValue = "true")
@ConditionalOnClass({PaginationInterceptor.class, JsqlParserCountOptimize.class})
public class MyBatisPlusConfiguration {

    /**
     * 分页配置
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(MyBatisPlusProperties myBatisPlusProperties) {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

}
