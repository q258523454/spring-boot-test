package com.sec.dao;

import com.sec.pojo.entity.GoodsSeckill;
import com.sec.pojo.dto.GoodsSeckillDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsSeckillMapper {

    int insert(GoodsSeckill record);

    int insertSelective(GoodsSeckill record);

    int updateByPrimaryKey(GoodsSeckill record);

    GoodsSeckill selectById(Long id);

    List<GoodsSeckill> selectAll();

    List<GoodsSeckillDetail> selectAllGoodsSeckillDetail();

    int reduceStockById(GoodsSeckill record);

}