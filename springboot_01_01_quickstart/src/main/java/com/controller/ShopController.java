package com.controller;

import com.dto.ResultDto;
import com.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /*
    * 根据店铺id查询数据
    * */
    @GetMapping("/{id}")
    public ResultDto queryShopById(@PathVariable("id") Long id){
        return shopService.queryById(id);
    }


}
