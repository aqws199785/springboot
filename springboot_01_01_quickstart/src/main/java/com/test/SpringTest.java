package com.test;

import com.entity.Shop;
import com.service.impl.ShopServiceImpl;
import com.utils.CacheClient;
import com.utils.RedisIdBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.utils.RedisConstants.*;

@SpringBootTest
class SpringTest {
    @Resource
    private CacheClient cacheClient;

    @Resource
    private ShopServiceImpl shopService;

    @Resource
    private RedisIdBuilder redisIdBuilder;

    @Test
    void testSaveShop() throws InterruptedException {
        Shop shop = shopService.getById(1L);
        // 实际使用
//        cacheClient.setWithLogicalExpire(SHOP_CACHE_PREFIX + 1L, shop, SHOP_CACHE_TTL, SHOP_CACHE_TTL_TIME_UNIT);
        // 测试使用 缓存预热
        cacheClient.setWithLogicalExpire(SHOP_CACHE_PREFIX + 1L, shop, 10L, TimeUnit.SECONDS);

    }

    @Test
    void test(){
        long id = redisIdBuilder.builder("shop");
        System.out.println(id);
    }

}
