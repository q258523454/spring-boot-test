package com.sec.service.impl;
import java.util.List;

import com.sec.pojo.entity.Order;

public interface OrderService {


    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Order record);



	List<Order> selectByUserIdAndSkGoodsId(Long userId,Long skGoodsId);


}
