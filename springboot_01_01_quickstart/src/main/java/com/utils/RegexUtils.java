package com.utils;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {

     /*
     * 校验字符串不匹配
     * 不匹配返回 true
     * 匹配返回 false
     */
    private static boolean mismatch(String str,String regex){
        // 校验字符串是否为 null empty 空白字符串组成
        if(StrUtil.isBlank(str)){
            return true;
        }
        return !str.matches(regex);
    }
    /*
    * 是否是无效的手机号
    * */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone,RegexPatterns.PHONE_REGEX);
    }
    public static boolean isEmailInvalid(String email){
        return mismatch(email,RegexPatterns.EMAIL_REGEX);
    }
    /*
    * 判断是否无效的验证码
    * */
    public static boolean isCodeInvalid(String verifyCode){
        return mismatch(verifyCode,RegexPatterns.VERIFY_CODE_REGEX);
    }
}