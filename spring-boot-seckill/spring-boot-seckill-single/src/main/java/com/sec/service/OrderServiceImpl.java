package com.sec.service;
import java.util.List;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.sec.pojo.entity.Order;
import com.sec.dao.OrderMapper;
import com.sec.service.impl.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public int insert(Order record) {
        return orderMapper.insert(record);
    }

    @Override
    public int insertSelective(Order record) {
        return orderMapper.insertSelective(record);
    }

    @Override
    public Order selectByPrimaryKey(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Order record) {
        return orderMapper.updateByPrimaryKey(record);
    }

	@Override
	public List<Order> selectByUserIdAndSkGoodsId(Long userId,Long skGoodsId){
		 return orderMapper.selectByUserIdAndSkGoodsId(userId,skGoodsId);
	}



}
