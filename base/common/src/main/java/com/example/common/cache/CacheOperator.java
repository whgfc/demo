package com.example.common.cache;

/**
 * 缓存接口
 * @param <T>
 */
public interface CacheOperator<T> {

    /**
     * 添加缓存
     * @param key
     * @param t
     */
    void put(String key, T t);

    /**
     * 添加缓存（带过期时间，单位是秒）
     * @param key
     * @param t
     * @param timeoutSeconds
     */
    void put(String key, T t, long timeoutSeconds);

    /**
     * 通过key获取数据
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 删除缓存
     * @param key
     */
    void remove(String... key);


    /**
     * 通用缓存的前缀，用于区分不同业务
     * @return
     */
    String getCommonKeyPrefix();
}
