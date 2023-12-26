package com.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*
* 秒杀优惠券
* */

@Getter
@Setter
@TableName("tb_seckill_voucher")
public class SecKillVoucher {
    // 优惠券id
    @TableId(value = "voucher_id")
    private Long voucherId;
    // 库存
    private Integer stock;
    //创建时间
    private LocalDateTime beginTime;
    //失效时间
    private LocalDateTime endTime;
    //更新时间
    private LocalDateTime updateTime;
}
