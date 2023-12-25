package com.utils;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dto.RedisData;
import com.entity.Shop;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.utils.RedisConstants.*;

@Component
public class CacheClient {
    public final StringRedisTemplate stringRedisTemplate;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(8);

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /*
     * redis设置一个key的值和过期时间

     * */
    public void set(String key, Object value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, timeUnit);
    }

    /*
     * 设置逻辑过期
     * time     逻辑过期的值
     * timeUtil 逻辑过期的单位
     * */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit timeUnit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTimeStamp(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /*
     * 解决缓存穿透
     * <T,ID> T <T,ID> 是输入参数类型 T 是输出参数类型
     * */
    public <T, ID> T queryWithPassThrough(String keyPrefix, ID id, Class<T> type, Function<ID, T> function, Long time, TimeUnit timeUtil) {
        String cacheKey = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(cacheKey);
        // 双重检查
        if (StrUtil.isNotBlank(json)) {
            return JSONUtil.toBean(json, type);
        }
        if (json != null) {
            return null;
        }
        // 该函数使用id 查询数据库返回一个对象
        T t = function.apply(id);
        // 如果没查到 将空值写入
        if (t == null) {
            stringRedisTemplate.opsForValue().set(cacheKey, "", time, timeUtil);
            return null;
        } else {
            String jsonStr = JSONUtil.toJsonStr(t);
            stringRedisTemplate.opsForValue().set(cacheKey, jsonStr, time, timeUtil);
            return t;
        }
    }

    /*
     * 解决缓存击穿
     * 需要将缓存预热：将带有逻辑过期字段的数据提前写入redis
     * */
    public <T, ID> T queryWithLogicalExpire(String keyPrefix, ID id, Class<T> type, String lockPrefix, Function<ID, T> function, Long time, TimeUnit timeUnit) {
        // TODO 获取缓存
        String cacheKey = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlank(json)) {
            // 如果为空 直接返回
            return null;
        }

        // TODO 验证缓存是否逻辑过期
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        T t = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTimeStamp = redisData.getExpireTimeStamp();
        LocalDateTime localDateTime = LocalDateTime.now();
        if (expireTimeStamp.isAfter(localDateTime)) {
            return t;
        }
        // TODO 逻辑过期后重新构建缓存
        String lockKey = lockPrefix + id;
        Boolean isLock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, COMMON_LOCK_VALUE, time, timeUnit);
        // 如果获取到锁 开启一个独立的线程执行缓存重建
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查询数据库构建缓存
                    T t1 = function.apply(id);
                    redisData.setData(t1);
                    redisData.setExpireTimeStamp(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
                    // 逻辑过期相当于永不过期 只不过添加了一个过期字段
                    stringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(redisData));
                    Thread.sleep(5000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    stringRedisTemplate.delete(lockKey);
                }
            });
        }

        return t;
    }

}
