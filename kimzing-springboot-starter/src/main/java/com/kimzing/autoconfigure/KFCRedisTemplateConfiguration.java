package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.KFCRedisTemplateProperties;
import com.kimzing.redis.KFCJsonRedisSerializer;
import com.kimzing.redis.KFCRedisTemplate;
import com.kimzing.redis.KFCStringRedisSerializer;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * KFCRedisTemplate配置.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020年07月19日
 */
@Configuration
@EnableConfigurationProperties({KFCRedisTemplateProperties.class})
@ConditionalOnClass({RedisTemplate.class, RedissonClient.class})
public class KFCRedisTemplateConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "kimzing.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
    public KFCRedisTemplate kfcRedisTemplate(RedissonClient redissonClient,
                                             KFCRedisTemplateProperties kfcRedisTemplateProperties,
                                             RedisConnectionFactory redisConnectionFactory) {
        String timePattern = kfcRedisTemplateProperties.getTimePattern();
        String prefix = kfcRedisTemplateProperties.getPrefix();

        KFCRedisTemplate kfcRedisTemplate = new KFCRedisTemplate(redissonClient, timePattern);
        kfcRedisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置key和value的序列化方式
        KFCStringRedisSerializer kfcStringRedisSerializer = new KFCStringRedisSerializer(prefix);
        kfcRedisTemplate.setKeySerializer(kfcStringRedisSerializer);
        kfcRedisTemplate.setHashKeySerializer(kfcStringRedisSerializer);
        KFCJsonRedisSerializer kfcJsonRedisSerializer = new KFCJsonRedisSerializer(timePattern);
        kfcRedisTemplate.setValueSerializer(kfcJsonRedisSerializer);
        kfcRedisTemplate.setHashValueSerializer(kfcJsonRedisSerializer);

        return kfcRedisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate redisTemplate( KFCRedisTemplateProperties kfcRedisTemplateProperties,
                                        RedisConnectionFactory redisConnectionFactory) {
        String timePattern = kfcRedisTemplateProperties.getTimePattern();
        String prefix = kfcRedisTemplateProperties.getPrefix();


        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置key和value的序列化方式
        KFCStringRedisSerializer kfcStringRedisSerializer = new KFCStringRedisSerializer(prefix);
        redisTemplate.setKeySerializer(kfcStringRedisSerializer);
        redisTemplate.setHashKeySerializer(kfcStringRedisSerializer);
        KFCJsonRedisSerializer kfcJsonRedisSerializer = new KFCJsonRedisSerializer(timePattern);
        redisTemplate.setValueSerializer(kfcJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(kfcJsonRedisSerializer);

        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(KFCRedisTemplateProperties kfcRedisTemplateProperties,
                                                   RedisConnectionFactory redisConnectionFactory) {
        String timePattern = kfcRedisTemplateProperties.getTimePattern();
        String prefix = kfcRedisTemplateProperties.getPrefix();

        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置key的序列化方式
        KFCStringRedisSerializer kfcStringRedisSerializer = new KFCStringRedisSerializer(prefix);
        stringRedisTemplate.setKeySerializer(kfcStringRedisSerializer);
        stringRedisTemplate.setHashKeySerializer(kfcStringRedisSerializer);

        return stringRedisTemplate;
    }

}
