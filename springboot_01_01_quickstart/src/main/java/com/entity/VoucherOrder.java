package com.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/*
* 优惠券订单
* 使用优惠券订单的表信息
* */

@Getter
@Setter
@TableName("tb_voucher_order")
public class VoucherOrder {
    private static final long serialVersionUid = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private Long userId;
    // 优惠券id
    private Long voucherId;

    private Integer payType;
    // 订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

    private LocalDateTime useTime;
    // 退款时间
    private LocalDateTime refundTime;
    // 更新时间
    private LocalDateTime updateTime;
}
