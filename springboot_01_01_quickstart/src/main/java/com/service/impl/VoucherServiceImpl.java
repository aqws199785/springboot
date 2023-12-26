package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.SecKillVoucher;
import com.entity.Voucher;
import com.mapper.VoucherMapper;
import com.service.SecKillVoucherService;
import com.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements VoucherService {

    @Autowired
    private SecKillVoucherService secKillVoucherService;

    /*
    * 该方法通过秒杀券接口调用保证写入的都是秒杀券
    * */
    @Override
    public void addSecKillVoucher(Voucher voucher) {
        SecKillVoucher secKillVoucher = new SecKillVoucher();
        secKillVoucher.setVoucherId(voucher.getId());
        secKillVoucher.setStock(voucher.getStock());
        secKillVoucher.setBeginTime(voucher.getBeginTime());
        secKillVoucher.setEndTime(voucher.getEndTime());
        secKillVoucher.setUpdateTime(voucher.getUpdateTime());
        // 将优惠券信息保存到 优惠券数据库
        save(voucher);
        // 保存秒杀优惠券
        secKillVoucherService.save(secKillVoucher);
    }
}
