package com.kimzing.redis;

import com.kimzing.utils.exception.ExceptionManager;
import com.kimzing.utils.json.JsonUtil;
import com.kimzing.utils.log.LogUtil;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;

/**
 * KimZing FanLongfei Custom Redis操作模板.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/19 00:02
 */
public class KFCRedisTemplate extends RedisTemplate {

    private String dateFormate;

    private RedissonClient redissonClient;

    public KFCRedisTemplate(RedissonClient redissonClient, String dateFormate) {
        this.redissonClient = redissonClient;
        this.dateFormate = dateFormate;
    }

    /**
     * 获取指定key的数据，并转换为对应的对象
     *
     * @param key
     * @param clazz
     * @return
     */
    public <K, V> V get(K key, Class<V> clazz) {
        Object object = this.opsForValue().get(key);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToBean(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 获取key下的elementKey的数据，并转换为对应的对象
     *
     * @param key
     * @param hashKey
     * @param clazz
     * @param <K>
     * @param <HK>
     * @param <E>
     * @return
     */
    public <K, HK, E> E getHashToBean(K key, HK hashKey, Class<E> clazz) {
        Object object = this.opsForHash().get(key, hashKey);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToBean(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 获取key下的多个elementKey的数据集合，并转换为对应的对象
     * <p>
     * elementValue的数据结构需保持一致
     * </p>
     *
     * @param key
     * @param hashKeys
     * @param clazz
     * @param <K>
     * @param <E>
     * @return
     */
    public <K, E> List<E> getHashToBean(K key, Collection hashKeys, Class<E> clazz) {
        Object object = this.opsForHash().multiGet(key, hashKeys);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToList(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 获取key下的elementKey的数据，并转换为对应的对象集合
     *
     * @param key
     * @param hashKey
     * @param clazz
     * @param <K>
     * @param <HK>
     * @param <E>
     * @return
     */
    public <K, HK, E> List<E> getHashToList(K key, HK hashKey, Class<E> clazz) {
        Object object = this.opsForHash().get(key, hashKey);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToList(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 根据key获取list的第一个元素，并转换为对应的对象，删除对应的值
     *
     * @param key
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> V leftPopList(K key, Class<V> clazz) {
        Object object = this.opsForList().leftPop(key);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToBean(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 根据key获取list的最后一个元素，并转换为对应的对象，删除对应的值
     *
     * @param key
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> V rightPopList(K key, Class<V> clazz) {
        Object object = this.opsForList().rightPop(key);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToBean(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 根据key随机获取set的一个元素，并转换为对应的对象，删除对应的值
     *
     * @param key
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> V popSet(K key, Class<V> clazz) {
        Object object = this.opsForSet().pop(key);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToBean(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 根据key随机获取set的count个元素，并转换为对应的对象集合，删除对应的值
     *
     * @param key
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> List<V> popSet(K key, long count, Class<V> clazz) {
        Object object = this.opsForSet().pop(key, count);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToList(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 根据key获取ZSET中从start到end的元素集合，并转换为对应的对象集合
     *
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> List<V> rangeZSet(K key, long start, long end, Class<V> clazz) {
        Object object = this.opsForZSet().range(key, start, end);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            String result = (String) object;
            return JsonUtil.jsonToList(result, clazz, dateFormate);
        } else {
            throw ExceptionManager.createByCodeAndMessage("REDIS_1001", "deserialization of value is not String Type!");
        }
    }

    /**
     * 分布式锁实现,同一时间只有一个能拿到锁
     *
     * <p>
     * RLock lock = KFCRedisTemplate.getlock("lock");
     * boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
     * if (res) {
     * try {
     * ...
     * } finally {
     * lock.unlock();
     * }
     * }
     * </p>
     *
     * @param lockKey
     * @return
     */
    public RLock getLock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }

    /**
     * 分布式锁实现，读写锁，可重复读，写时会阻塞其他读写
     *
     * <p>
     * RReadWriteLock rwlock = this.getReadWriteLock("lock");
     * rwlock.readLock().lock();
     * // 或
     * rwlock.writeLock().lock();
     * <p>
     * // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
     * boolean res = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
     * // 或
     * boolean res = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);
     * ...
     * lock.unlock();
     * </p>
     *
     * @param lockKey
     * @return
     */
    public RReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

    /**
     * 初始化布隆过滤器
     *
     * <p>
     *         // 向布隆过滤器中添加一个元素
     *         bloomFilter.add(T object);
     *         // 查看布隆过滤器中是否存在该元素
     *         bloomFilter.contains(T object);
     * </p>
     *
     * @param bloomKey
     * @param expectedInsertions 预计统计元素个数
     * @param falseProbability   期望误差率
     * @param <T>
     * @return
     */
    public <T> RBloomFilter<T> initBloomFilter(String bloomKey, long expectedInsertions, double falseProbability) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(bloomKey);
        // 尝试初始化布隆过滤器
        boolean isSuccess = bloomFilter.tryInit(expectedInsertions, falseProbability);
        if (isSuccess) {
            return bloomFilter;
        }
        // 查看是否已存在布隆过滤器
        RBloomFilter<T> exsitBloomFilter = redissonClient.getBloomFilter(bloomKey);
        if (exsitBloomFilter != null) {
            LogUtil.warn("该布隆过滤器已经初始化，无法生效对应的参数设置~");
            return exsitBloomFilter;
        }
        LogUtil.error("初始化布隆过滤器失败！ bloomKey:[{}]", bloomKey);
        return null;
    }

}
