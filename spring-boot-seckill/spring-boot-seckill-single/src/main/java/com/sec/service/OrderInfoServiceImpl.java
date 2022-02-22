package com.sec.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.sec.dao.OrderInfoMapper;
import com.sec.pojo.entity.OrderInfo;
import com.sec.service.impl.OrderInfoService;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Override
    public int insert(OrderInfo record) {
        return orderInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(OrderInfo record) {
        return orderInfoMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(OrderInfo record) {
        return orderInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public OrderInfo selectByPrimaryKey(Long id) {
        return orderInfoMapper.selectByPrimaryKey(id);
    }
}

