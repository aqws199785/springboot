//package com.test;
//
//import com.entity.Shop;
//import com.service.impl.ShopServiceImpl;
//import com.utils.CacheClient;
//import com.utils.RedisIdBuilder;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.junit.jupiter.api.Test;
//
//import javax.annotation.Resource;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import static com.utils.RedisConstants.*;
//
//@SpringBootTest
//class SpringTest {
//    @Resource
//    private CacheClient cacheClient;
//
//    @Resource
//    private ShopServiceImpl shopService;
//
//    @Resource
//    private RedisIdBuilder redisIdBuilder;
//
//    private ExecutorService executorService = Executors.newFixedThreadPool(300);
//
//
//
//    @Test
//    void testSaveShop() throws InterruptedException {
//        Shop shop = shopService.getById(1L);
//        // 实际使用
////        cacheClient.setWithLogicalExpire(SHOP_CACHE_PREFIX + 1L, shop, SHOP_CACHE_TTL, SHOP_CACHE_TTL_TIME_UNIT);
//        // 测试使用 缓存预热
//        cacheClient.setWithLogicalExpire(SHOP_CACHE_PREFIX + 1L, shop, 10L, TimeUnit.SECONDS);
//
//    }
//
//    @Test
//    void test() {
//        long id = redisIdBuilder.builder("shop");
//        System.out.println(id);
//    }
//
//    /*
//     * 测试生成300*100个业务全局唯一id生成时间
//     * */
//    @Test
//    void testRedisIdBuilder() throws InterruptedException {
//
//        CountDownLatch countDownLatch = new CountDownLatch(300);
//        Runnable task = () -> {
//            for (int i = 0; i < 100; i++) {
//                long id = redisIdBuilder.builder("order");
////                System.out.println("id : " + id);
//            }
//            // 计数减少
//            countDownLatch.countDown();
//        };
//        long beginTime = System.currentTimeMillis();
//        for (int i = 0; i < 300; i++) {
//            executorService.submit(task);
//        }
//        // 该方法是一个阻塞的 要等待到计数减少到为0
//        countDownLatch.await();
//        long endTime = System.currentTimeMillis();
//        System.out.println("time : "+(endTime-beginTime));
//
//    }
//
//}
