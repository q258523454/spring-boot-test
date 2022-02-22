package com.sec.dao;

import com.sec.pojo.entity.OrderInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoMapper {
    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(OrderInfo record);
}