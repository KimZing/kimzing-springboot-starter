package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.FileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 配置文件自动加载配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/26 10:57
 */
@Configuration
@EnableConfigurationProperties({FileProperties.class})
@PropertySource(
        value = {"${kimzing.property.files[0]}", "${kimzing.property.files[1]}",
                "${kimzing.property.files[2]}", "${kimzing.property.files[3]}",
                "${kimzing.property.files[4]}"},
        ignoreResourceNotFound = true,
        encoding = "UTF-8")
@ConditionalOnBean(value = Environment.class)
public class PropertyFileAutoConfiguration {

}
