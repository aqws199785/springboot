package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.ResultDto;
import com.entity.VoucherOrder;

public interface VoucherOrderService extends IService<VoucherOrder> {
    ResultDto secKIllVoucher(Long voucherId);
}
