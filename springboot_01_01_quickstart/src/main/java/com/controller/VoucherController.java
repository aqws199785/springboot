package com.controller;


import com.dto.ResultDto;
import com.entity.Voucher;
import com.service.impl.VoucherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* 代金券
* */

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherServiceImpl voucherService;

    @PostMapping("/secKill")
    public ResultDto addSecKillVoucher(@RequestBody Voucher voucher){
        voucherService.addSecKillVoucher(voucher);
        return ResultDto.ok(voucher.getId());
    }
}
