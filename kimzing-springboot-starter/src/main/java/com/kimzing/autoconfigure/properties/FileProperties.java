package com.kimzing.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring容器加载的属性文件.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/25 16:29
 */
@Data
@ConfigurationProperties(prefix = "kimzing.property", ignoreUnknownFields = true)
public class FileProperties {

    /**
     * 属性文件名，支持<=3个文件，示例:["message.propertes","exception.properties","error.properties"]
     */
    private String[] files;

}
