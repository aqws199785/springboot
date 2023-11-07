package com.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
* 创建一个对象并 添加有参和无参构造方法
* */
public class User {
    private String name;
    private int age;
}
