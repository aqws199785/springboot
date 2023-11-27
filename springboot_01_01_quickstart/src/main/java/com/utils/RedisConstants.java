package com.utils;

public class RedisConstants {

    /*
    * 用户登录的业务前缀 用于在请求中区分业务
    */
    public static final String LOGIN_CODE_KEY = "login:code:";

    /*
    * 登录验证码生存时间 Time To Live 存活时间
    * */
    public static final Long LOGIN_CODE_TTL=2L;

    /*
    * 登录用户Token 前缀 用以区分业务
    * */
    public static final String LOGIN_TOKEN_KEY = "login:token:";

    /*
    * 登录用户Token TTL
    * */
    public static final Long LOGIN_TOKEN_TTL = 30L;
}
