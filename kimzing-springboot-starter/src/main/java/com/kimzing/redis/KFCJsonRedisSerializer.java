package com.kimzing.redis;

import com.kimzing.utils.json.JsonUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KFCJsonRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    private final String dateFormate;

    public KFCJsonRedisSerializer(String dateFormate) {
        this(dateFormate, StandardCharsets.UTF_8);
    }

    public KFCJsonRedisSerializer(String dateFormate, Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
        this.dateFormate = dateFormate;
    }

    @Override
    public byte[] serialize(Object t) {
        if (t == null) {
            return new byte[0];
        }
        return JsonUtil.beanToJson(t, dateFormate).getBytes(charset);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new String(bytes, charset);
    }

}
