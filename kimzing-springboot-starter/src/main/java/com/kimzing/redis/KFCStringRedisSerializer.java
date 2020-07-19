package com.kimzing.redis;

import com.kimzing.utils.exception.ExceptionManager;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KFCStringRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    private final String prefix;

    public KFCStringRedisSerializer(String prefix) {
        this(prefix, StandardCharsets.UTF_8);
    }

    public KFCStringRedisSerializer(String prefix, Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.prefix = prefix;
        this.charset = charset;
    }

    @Override
    public byte[] serialize(@Nullable Object s) {
        if (s == null) {
            return null;
        }
        // 从配置文件读取该服务的redis公共前缀
        if (prefix == null) {
            return s.toString().getBytes(charset);
        }
        // 拼接带前缀的key
        s = prefix + ":" + s;
        return s.toString().getBytes(charset);
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String s = new String(bytes, charset);
        // 从配置文件读取该服务的redis公共前缀
        if (prefix == null) {
            return s;
        }

        // 去除拼接的服务的redis公共前缀
        if (s.startsWith(prefix)) {
            return s.substring(prefix.length() + 1);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "REDIS缺少通用前缀");
        }
    }

}
