package com.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.RedisData;
import com.dto.ResultDto;
import com.entity.Shop;
import com.mapper.ShopMapper;
import com.service.ShopService;
import com.utils.CacheClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CacheClient cacheClient;

    @Override
    public ResultDto queryById(Long id) {
        String shopKey = SHOP_CACHE_PREFIX + id;
        //  Shop shop = cacheClient.queryWithPassThrough(SHOP_CACHE_PREFIX, id, Shop.class, this::getById, SHOP_CACHE_TTL, SHOP_CACHE_TTL_TIME_UNIT);
        Shop shop = cacheClient.queryWithLogicalExpire(SHOP_CACHE_PREFIX, id, Shop.class, SHOP_LOCK_PREFIX, this::getById, 10L, TimeUnit.SECONDS);
        if (shop == null) {
            return ResultDto.fail("店铺不存在");
        }

        return ResultDto.ok(shop);
    }

    @Override
    public ResultDto queryShopByType(Integer typeId, Integer current, Double x, Double y) {
        Page<Shop> shopPage = query()
                .eq("type_id", typeId)
                .page(new Page<>(current, 10));
        return ResultDto.ok(shopPage);
    }


}
