package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.MinioProperties;
import com.kimzing.minio.MinioService;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio自动配置类.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/20 01:14
 */
@Configuration
@EnableConfigurationProperties({MinioProperties.class})
@ConditionalOnProperty(prefix = "kimzing.minio", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass({MinioClient.class})
public class MinioAutoConfiguration {

    @Bean
    public MinioService minioService(MinioProperties minioProperties) throws Exception {
        return new MinioService(minioClient(minioProperties), minioProperties);
    }

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        return minioClient;
    }

}
