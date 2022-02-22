package com.sec.dao;

import com.sec.pojo.entity.Goods;
import org.springframework.stereotype.Repository;


@Repository
public interface GoodsMapper {
    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Goods record);
}