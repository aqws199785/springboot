package com.service.impl;

import com.dto.ResultDto;
import com.service.ShopService;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;
import static com.utils.ServiceConstants.SHOP_CACHE_PREFIX;

public class ShopServiceImpl implements ShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResultDto queryById(Long id) {
        String shopKey = SHOP_CACHE_PREFIX + id;
        Object o = stringRedisTemplate.opsForHash().entries(shopKey);

        return ResultDto.ok();
    }
}
