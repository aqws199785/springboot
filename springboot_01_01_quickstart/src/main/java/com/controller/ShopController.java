package com.controller;

import com.dto.ResultDto;
import com.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /*
     * 根据店铺id查询数据
     * */
    @GetMapping("/{id}")
    public ResultDto queryShopById(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }

    @GetMapping("/of/type")
    public ResultDto queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "x", required = false) Double x,
            @RequestParam(value = "y", required = false) Double y
    ) {
        return shopService.queryShopByType(typeId,current,x,y);
    }

}
