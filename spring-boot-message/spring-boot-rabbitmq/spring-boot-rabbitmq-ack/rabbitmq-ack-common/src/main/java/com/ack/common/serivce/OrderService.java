package com.ack.common.serivce;

import java.util.List;
import com.ack.common.entity.Order;
public interface OrderService {


    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    int updateBatch(List<Order> list);

}
