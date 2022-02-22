package com.sec.service.impl;

import com.sec.pojo.entity.OrderInfo;

public interface OrderInfoService {


    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);
}

