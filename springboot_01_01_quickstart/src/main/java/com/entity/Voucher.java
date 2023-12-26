package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*
 * 优惠券
 * */
@Getter
@Setter
@TableName("tb_voucher")
public class Voucher {
    // 自增主键
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    // 店铺id
    private Long shopId;
    // 标题
    private String title;
    // 二级标题
    private String subTitle;
    // 使用规则
    private String rules;
    // 支付金额
    private Long payValue;
    //抵扣金额
    private Long actualValue;
    // 优惠券类型
    private Integer type;
    // 库存
    @TableField(exist = false)
    private Integer stock;
    // 生效时间
    @TableField(exist = false)
    private LocalDateTime beginTime;
    // 失效时间
    @TableField(exist = false)
    private LocalDateTime endTime;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
