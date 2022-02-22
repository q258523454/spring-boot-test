package com.sec.service.impl;

import com.sec.pojo.entity.GoodsSeckill;
import com.sec.pojo.entity.User;
import com.sec.pojo.dto.GoodsSeckillDetail;

import java.util.List;

public interface GoodsSeckillService {


    int insert(GoodsSeckill record);

    int insertSelective(GoodsSeckill record);

    int updateByPrimaryKey(GoodsSeckill record);

    GoodsSeckill selectById(Long id);

    List<GoodsSeckill> selectAll();

    List<GoodsSeckillDetail> selectAllGoodsSeckillDetail();

    boolean doSeckill(long user, GoodsSeckill goodsSeckill);

    String getSeckillResult(User user, long skGoodsId);

}

