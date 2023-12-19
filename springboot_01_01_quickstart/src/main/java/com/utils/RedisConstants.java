package com.utils;

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
     * 互斥锁业务前缀
     * 互斥锁值
     * 互斥锁TTL
     * */
    public static final String SHOP_LOCK_PREFIX = "lock:shop:";
    public static final String SHOP_LOCK_VALUE = "lock";
    public static final Long SHOP_LOCK_TTL = 10L;
}
