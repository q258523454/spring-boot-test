package com.sec.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.sec.pojo.entity.Goods;
import com.sec.dao.GoodsMapper;
import com.sec.service.impl.GoodsService;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public int insert(Goods record) {
        return goodsMapper.insert(record);
    }

    @Override
    public int insertSelective(Goods record) {
        return goodsMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(Goods record) {
        return goodsMapper.updateByPrimaryKey(record);
    }

    @Override
    public Goods selectByPrimaryKey(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
}

