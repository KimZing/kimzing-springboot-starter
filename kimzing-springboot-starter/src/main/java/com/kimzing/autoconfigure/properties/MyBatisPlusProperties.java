package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MyBatisPlus配置属性.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/5 16:43
 */
@Data
@ConfigurationProperties(prefix = "kimzing.mybatis-plus", ignoreUnknownFields = true)
public class MyBatisPlusProperties {

    private PageProperties page = new PageProperties();

    private PerformanceProperties performance = new PerformanceProperties();

    @Data
    public static class PageProperties {
        /**
         * 控制开关
         */
        private Boolean enabled;

        /**
         * 开启count的join优化
         */
        private Boolean optimizeJoin = true;

    }

    @Data
    public static class PerformanceProperties {

        /**
         * 控制开关
         */
        private Boolean enabled;

        /**
         * 是否格式化SQL
         */
        private Boolean formate = false;

        /**
         * 最长执行时间
         */
        private Long maxTime = 1000L;

    }

}
