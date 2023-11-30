package com.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.ResultDto;
import com.entity.Shop;
import com.mapper.ShopMapper;
import com.service.ShopService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.utils.ServiceConstants.SHOP_CACHE_PREFIX;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResultDto queryById(Long id) {
        String shopKey = SHOP_CACHE_PREFIX + id;
        // (1) 查询店铺redis缓存
        Map<Object, Object> shopEntries = stringRedisTemplate.opsForHash().entries(shopKey);
        // (2)判断缓存是否存在
        // 不存在 查询数据库 数据库存在写入redis 数据库不存在 创建一个新的shop写入redis
        // 需要注意数据一致性问题 需要额外个应用程序捕获数据库店铺数据变化写入redis
        Shop shop;
        if (shopEntries.isEmpty()) {
            shop = getById(id);
            shop = shop == null ? new Shop() : shop;
            Map<String, Object> shopMap = BeanUtil.beanToMap(
                    shop,
                    new HashMap<>(),
                    CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((field, value) -> value == null ? null : value.toString())
            );
            stringRedisTemplate.opsForHash().putAll(shopKey, shopMap);
        } else {
            shop = BeanUtil.fillBeanWithMap(shopEntries, new Shop(), true);
        }
        return ResultDto.ok(shop);
    }
}
