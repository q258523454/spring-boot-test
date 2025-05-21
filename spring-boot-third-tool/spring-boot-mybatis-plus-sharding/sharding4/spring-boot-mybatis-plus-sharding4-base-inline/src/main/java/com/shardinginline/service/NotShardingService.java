package com.shardinginline.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shardinginline.dao.NotShardingPlusMapper;
import com.shardinginline.entity.NotSharding;

import org.springframework.stereotype.Service;

@Service
public class NotShardingService extends ServiceImpl<NotShardingPlusMapper, NotSharding> {
}
