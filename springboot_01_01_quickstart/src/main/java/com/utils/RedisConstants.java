package com.utils;

import java.util.concurrent.TimeUnit;

public class RedisConstants {

    /*
     * 用户登录的业务前缀 用于在请求中区分业务
     * 登录验证码生存时间 Time To Live 存活时间
     */
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;

    /*
     * 登录用户Token 前缀 用以区分业务
     * 登录用户Token TTL
     * */
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    public static final Long LOGIN_TOKEN_TTL = 30L;


    /*
     * 店铺业务前缀
     * 店铺缓存前缀
     * */
    public static final String SHOP_SERVICE_PREFIX = "shop";
    public static final String SHOP_CACHE_PREFIX = "cache:shop:";
    public static final Long SHOP_CACHE_TTL = 10L;
    public static final TimeUnit SHOP_CACHE_TTL_TIME_UNIT = TimeUnit.MINUTES;
    /*
     * 互斥锁业务前缀
     * 互斥锁值
     * 互斥锁TTL
     * */
    public static final String SHOP_LOCK_PREFIX = "lock:shop:";
    public static final String SHOP_LOCK_VALUE = "lock";
    public static final Long SHOP_LOCK_TTL = 10L;
    public static final Long SHOP_LOGICAL_EXPIRE_TTL = 10L;

    /*
    * 通用的配置
    * */
    public static final String COMMON_LOCK_VALUE="lock";

}
