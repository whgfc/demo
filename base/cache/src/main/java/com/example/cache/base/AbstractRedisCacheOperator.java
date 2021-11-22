package com.example.cache.base;

import com.example.common.cache.CacheOperator;
import com.google.common.collect.Lists;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * redis缓存骨架类
 */
public abstract class AbstractRedisCacheOperator<T> implements CacheOperator<T> {

    private RedisTemplate<String, T> redisTemplate;

    public AbstractRedisCacheOperator (RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(String key, T t) {
        redisTemplate.boundValueOps(this.getCommonKeyPrefix() + key).set(t);
    }

    @Override
    public void put(String key, T t, long timeoutSeconds) {
        redisTemplate.boundValueOps(this.getCommonKeyPrefix() + key).set(t);
    }

    @Override
    public T get(String key) {
        return redisTemplate.boundValueOps(this.getCommonKeyPrefix() + key).get();
    }

    @Override
    public void remove(String... key) {
        ArrayList<String> keys = Lists.newArrayList(key);
        redisTemplate.delete(keys.stream().distinct().map(i -> getCommonKeyPrefix() + i).collect(Collectors.toList()));
    }
}
