package com.shardinginline.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shardinginline.entity.NotSharding;

import org.springframework.stereotype.Repository;


@Repository
public interface NotShardingPlusMapper extends BaseMapper<NotSharding> {
}