package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.ResultDto;
import com.entity.SecKillVoucher;
import com.entity.VoucherOrder;
import com.mapper.VoucherOrderMapper;
import com.service.SecKillVoucherService;
import com.service.VoucherOrderService;
import com.utils.RedisIdBuilder;
import com.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements VoucherOrderService {

    @Autowired
    private SecKillVoucherService secKillVoucherService;

    @Autowired
    private RedisIdBuilder redisIdBuilder;

//    @Transactional
    @Override
    public ResultDto secKIllVoucher(Long voucherId) {
        System.out.println(voucherId);
        SecKillVoucher secKillVoucher = secKillVoucherService.getById(voucherId);
        System.out.println(secKillVoucher.toString());
        // 开始时间在当前时间之后
        if (secKillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return ResultDto.fail("秒杀暂未开始");
        }
        if (secKillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            return ResultDto.fail("秒杀已经结束");
        }
        if (secKillVoucher.getStock() < 1) {
            return ResultDto.fail("库存不足");
        }
        boolean success = secKillVoucherService.update()
                .setSql("stock=stock-1")
                .eq("voucher_id", voucherId).gt("stock", 0)
                .update();
        if (!success) {
            return ResultDto.fail("库存不足");
        }
        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdBuilder.builder("order");

        voucherOrder.setId(orderId);
        voucherOrder.setUserId(UserHolder.getUser().getId());
        voucherOrder.setVoucherId(voucherId);

        //订单写入数据库
        save(voucherOrder);

        return ResultDto.ok(orderId);
    }
}
