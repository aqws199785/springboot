package com.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.ResultDto;
import com.entity.ShopType;
import com.mapper.ShopTypeMapper;
import com.service.ShopTypeService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {

    @Override
    public ResultDto queryShopType() {
        QueryChainWrapper<ShopType> shopTypeQueryChainWrapper = query().select("name").orderByAsc("sort");
        List<ShopType> shopTypeList = shopTypeQueryChainWrapper.list();
        return ResultDto.ok(shopTypeList);
    }
}
