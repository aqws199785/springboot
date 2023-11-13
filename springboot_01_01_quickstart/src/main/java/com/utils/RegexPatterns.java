package com.utils;

/*
 * 一些常用的验证规则
 * 验证秘钥是否符合规则
 * */
public class RegexPatterns {
    //   (1) 手机号正则
    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    //   (2) 邮箱正则
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    //   (3) 密码正则
    //   字符4-32位
    public static final String PASSWORD_REGEX = "^\\w{4,32}$";
    //   (4) 验证码正则
    public static final String VERIFY_CODE_REGEX = "^[a-zA-Z\\d]{6}$";

}
