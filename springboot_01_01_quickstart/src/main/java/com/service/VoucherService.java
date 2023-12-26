package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.Voucher;

public interface VoucherService extends IService<Voucher> {
    // 添加秒杀优惠券
    void addSecKillVoucher(Voucher voucher);
}
