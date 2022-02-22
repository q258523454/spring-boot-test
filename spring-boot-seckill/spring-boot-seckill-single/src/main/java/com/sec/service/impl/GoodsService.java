package com.sec.service.impl;

import com.sec.pojo.entity.Goods;

public interface GoodsService {


    int insert(Goods record);

    int insertSelective(Goods record);

    int updateByPrimaryKey(Goods record);

    Goods selectByPrimaryKey(Long id);


}

