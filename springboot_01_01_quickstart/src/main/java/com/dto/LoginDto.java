package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
* 一个管理登录界面的
*
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String phone;
    private String verifyCode;
    private String password;
}
