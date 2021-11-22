package com.example.cache;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.cache.base.AbstractRedisCacheOperator;
import com.example.common.cache.CacheOperator;
import com.example.common.consts.SymbolConstant;
import com.example.common.login.SysLoginUser;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class UserCache extends AbstractRedisCacheOperator<SysLoginUser> implements CacheOperator<SysLoginUser> {

    /**
     * 登录用户缓存前缀
     */
    public static final String LOGIN_USER_CACHE_PREFIX = "login:user:";

    private final RedisTemplate<String, SysLoginUser> redisTemplate;

    public UserCache(RedisTemplate<String, SysLoginUser> redisTemplate) {
        super(redisTemplate);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 返回指定用户的当前登录的所有完整key
     * @param userId
     * @return
     */
    public Collection<String> getAllCompleteKeys(Long userId) {
        Set<String> keys = redisTemplate.keys(getCommonKeyPrefix() + userId + SymbolConstant.COLON + SymbolConstant.ASTERISK);
        if (CollectionUtil.isNotEmpty(keys)) {
            return keys;
        }
        return Collections.emptyList();
    }

    /**
     * 返回指定用户的当前登录的所有session id
     * @param userId
     * @return
     */
    public Collection<String> getAllSessionKeys(Long userId) {
        Collection<String> allCompleteKeys = getAllCompleteKeys(userId);
        if (CollectionUtil.isNotEmpty(allCompleteKeys)) {
            return allCompleteKeys.stream().map(key -> StrUtil.removePrefix(key, getCommonKeyPrefix() + userId + SymbolConstant.COLON))
                    .collect(Collectors.toList());
        }
        return allCompleteKeys;
    }

    public Collection<SysLoginUser> getAllValues(Long userId) {
        Collection<String> allCompleteKeys = getAllCompleteKeys(userId);
        if (CollectionUtil.isNotEmpty(allCompleteKeys)) {
            return redisTemplate.opsForValue().multiGet(allCompleteKeys);
        }
        return Collections.emptyList();
    }

    public Map<String, SysLoginUser> getAllKeyValues(Long userId) {
        Collection<String> allKeys = this.getAllSessionKeys(userId);
        Map<String, SysLoginUser> results = new HashMap<>();
        for (String key : allKeys) {
            results.put(key, this.get(key));
        }
        return results;
    }

    @Override
    public String getCommonKeyPrefix() {
        return LOGIN_USER_CACHE_PREFIX;
    }
}
