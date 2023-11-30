package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.ResultDto;
import com.entity.ShopType;

import java.util.List;

public interface ShopTypeService extends IService<ShopType> {
    ResultDto queryShopType();
}
