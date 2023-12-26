package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.ResultDto;
import com.entity.Shop;

public interface ShopService extends IService<Shop> {
    ResultDto queryById(Long id);

    ResultDto queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
