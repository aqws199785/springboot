package com.controller;


import com.dto.ResultDto;
import com.service.VoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

    @Autowired
    private VoucherOrderService voucherOrderService;

    @PostMapping("/seckill/{id}")
    public ResultDto secKillVoucher(@PathVariable("id") Long voucherId) {
        System.out.println("controller:" + voucherId);
        return voucherOrderService.secKIllVoucher(voucherId);
    }
}
