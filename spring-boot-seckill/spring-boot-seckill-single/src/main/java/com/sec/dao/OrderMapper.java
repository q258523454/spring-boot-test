package com.sec.dao;
import org.apache.ibatis.annotations.Param;

import com.sec.pojo.entity.Order;

import java.util.List;

public interface OrderMapper {
    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Order record);

    List<Order> selectByUserIdAndSkGoodsId(@Param("userId")Long userId,@Param("skGoodsId")Long skGoodsId);

}