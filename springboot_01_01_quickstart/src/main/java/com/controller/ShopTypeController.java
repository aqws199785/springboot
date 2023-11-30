package com.controller;

import com.dto.ResultDto;
import com.service.ShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
*   店铺类型接口
*   注意接口名称@RequestMapping("/shop-type") 中不能有大写字母
* */
@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {

    @Autowired
    private ShopTypeService shopTypeService;

    //  展示店铺类型名称
    @GetMapping("list")
    public ResultDto queryShopType(){
        return shopTypeService.queryShopType();
    }
}
