package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* 用户
* Data Transfer Object：数据传输对象
* 用以存储返回前端用户的信息 避免数据泄漏
* */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    // id
    private int id;
    // 昵称
    private String nickName;
    // 图标（头像）
    private String icon;
}
