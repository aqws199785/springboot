package com.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.RedisData;
import com.dto.ResultDto;
import com.entity.Shop;
import com.mapper.ShopMapper;
import com.service.ShopService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


import static com.utils.RedisConstants.*;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 创建一个线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    @Override
    public ResultDto queryById(Long id) {
        String shopKey = SHOP_CACHE_PREFIX + id;
        // (1) 查询店铺redis缓存
//        Map<Object, Object> shopEntries = stringRedisTemplate.opsForHash().entries(shopKey);
        // (2)判断缓存是否存在
        // 不存在 查询数据库 数据库存在写入redis 数据库不存在 创建一个新的shop写入redis
        // 需要注意数据一致性问题 需要额外个应用程序捕获数据库店铺数据变化写入redis
//        Shop shop;
//        if (shopEntries.isEmpty()) {
//            shop = getById(id);
//            shop = shop == null ? new Shop() : shop;
//            Map<String, Object> shopMap = BeanUtil.beanToMap(
//                    shop,
//                    new HashMap<>(),
//                    CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((field, value) -> value == null ? null : value.toString())
//            );
//            stringRedisTemplate.opsForHash().putAll(shopKey, shopMap);
//        } else {
//            shop = BeanUtil.fillBeanWithMap(shopEntries, new Shop(), true);
//        }
        Shop shop = queryWithMutex(id);
        return ResultDto.ok(shop);
    }

    /*
     * 解决缓存穿透和缓存击穿
     * */
    public Shop queryWithMutex(Long id) {
        String cacheKey = SHOP_CACHE_PREFIX + id;


        // TODO 实现缓存重建
        //(1)获取互斥锁
        String lockKey = SHOP_LOCK_PREFIX + id;
        Shop shop = null;
        // 获取互斥锁 如果不存在则添加设置
        try {
            boolean isLoop = true;
            while (isLoop) {
                String shopJson = stringRedisTemplate.opsForValue().get(cacheKey);
                // 如果不为null、空白、不为仅为空字符串组成
                if (StrUtil.isNotBlank(shopJson)) {
                    shop = JSONUtil.toBean(shopJson, Shop.class);
                    return shop;
                }
                // 如果上一步获取到缓存却没有返回 返回一个报错信息
                if (shopJson != null) {
                    return null;
                }

                Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "lock", 10, TimeUnit.SECONDS);
                boolean isLock = aBoolean.booleanValue();
                if (isLock) {
                    // 获取到锁跳出循环
                    isLoop = false;
                } else {
                    // 没有获取到则休眠
                    Thread.sleep(50);
                }
            }
            shop = getById(id);
            // 模拟重建延迟
            Thread.sleep(200);
            // 解决缓存穿透 缓存没有数据所以返回null
            if (shop == null) {
                // 查询数据库不存在 缓存写入一个空值
                stringRedisTemplate.opsForValue().set(cacheKey, SHOP_LOCK_VALUE, SHOP_LOCK_TTL, TimeUnit.MINUTES);
                return null;
            } else {
                // 查询数据库存在 将查询数据写入redis
                stringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(shop));
                return shop;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stringRedisTemplate.delete(lockKey);
        }
        return shop;
    }

    /*
     *逻辑过期
     * */
    public Shop queryWithLogicalExpire(Long id) {

        String cacheKey = SHOP_CACHE_PREFIX + id;
        String shopJson = stringRedisTemplate.opsForValue().get(cacheKey);
        // 如果未命中
        if (StrUtil.isBlank(shopJson)) {
            // 不存在直接返回
            return null;
        }
        //TODO 命中反序列化Json
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        JSONObject data = (JSONObject) redisData.getData();
        LocalDateTime expireTimeStamp = redisData.getExpireTimeStamp();
        AtomicReference<Shop> shop = new AtomicReference<>(JSONUtil.toBean(data, Shop.class));

        //TODO 判断是否过期

        if (expireTimeStamp.isAfter(LocalDateTime.now())) {
            return shop.get();
        }
        //TODO 缓存重建
        String shopLockKey = SHOP_LOCK_PREFIX + id;
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(shopLockKey, SHOP_LOCK_VALUE, SHOP_LOCK_TTL, TimeUnit.SECONDS);
        boolean isLock = aBoolean.booleanValue();
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                shop.set(getById(id));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                redisData.setData(shop);
                redisData.setExpireTimeStamp(LocalDateTime.now().plusSeconds(SHOP_LOGICAL_EXPIRE_TTL));
                stringRedisTemplate.opsForValue().set(SHOP_CACHE_PREFIX + id, JSONUtil.toJsonStr(redisData));
                stringRedisTemplate.delete(shopLockKey);
            });
        }

        return shop.get();
    }

    /*
     * 将查询到的店铺数据写入redis并加上逻辑过期时间
     * redis缓存预热 预先将热度较高的数据写入redis
     * */
    public void saveShopToRedis(Long id, Long expireSeconds) {
        Shop shop = getById(id);
        RedisData redisData = new RedisData();
        redisData.setData(shop);
        redisData.setExpireTimeStamp(LocalDateTime.now().plusSeconds(expireSeconds));
        stringRedisTemplate.opsForValue().set(SHOP_CACHE_PREFIX + id, JSONUtil.toJsonStr(redisData));
    }

}
