package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false) //false 时生成equals hashcode时不包含父类的属性 为true反正
@Accessors(chain = true) //为true是调用setter()时返回一个对象
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 自增主键
    * */
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    private String phone;
    private String password;
    private String nickName;
    private String icon; // 头像
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
