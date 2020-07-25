package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * web相关配置
 */
@Data
@ConfigurationProperties(prefix = "kimzing.web", ignoreUnknownFields = true)
public class WebProperties {

    /**
     * 支持的参数解析
     */
    private ParamResolverProperties resolver;

    /**
     * RestTemplate属性配置
     */
    private RestTemplateProperties restTemplate;

    /**
     * Info接口信息
     */
    private InfoProperties info;

    /**
     * Controller异常切入
     */
    private AdviceProperties advice;

    /**
     * 请求日志打印
     */
    private LogProperties log;

    /**
     * 跨域配置
     */
    private CorsProperties cors = new CorsProperties();

    /**
     * URL与Cookie转义配置
     */
    private EscapeProperties escape = new EscapeProperties();

    @Data
    public static class ParamResolverProperties {
        /**
         * Get方法Json参数解析
         */
        private JsonProperties json;

    }

    @Data
    public static class JsonProperties {
        private Boolean enabled;
    }

    @Data
    public static class AdviceProperties {
        /**
         * 异常Advice开关
         */
        private Boolean enabled;

    }

    @Data
    public static class LogProperties {
        /**
         * 日志Advice开关
         */
        private Boolean enabled;

        /**
         * 切入表达式
         */
        private String execution;

    }

    @Data
    public static class InfoProperties {

        /**
         * 接口路径, default: /info
         */
        private String path;

        /**
         * Info接口开关
         */
        private Boolean enabled;

        /**
         * 接口响应内容
         */
        private Map<String, Object> params;
    }

    @Data
    public static class RestTemplateProperties {
        /**
         * RestTemplate开关
         */
        private Boolean enabled;
    }

    @Data
    public static class CorsProperties {
        /**
         * 跨域开关
         */
        private Boolean enabled;

        /**
         * 允许的域
         */
        private String[] origins = new String[]{"*"};
    }

    @Data
    public static class EscapeProperties {
        /**
         * URL与Cookie转义开关
         */
        private Boolean enabled = true;

    }

}