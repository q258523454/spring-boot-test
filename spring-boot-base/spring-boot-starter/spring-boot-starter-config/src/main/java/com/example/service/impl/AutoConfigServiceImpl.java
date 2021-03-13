package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.entity.AutoConfigEntity;
import com.example.service.AutoConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhangj
 * @Date: 2019-09-11
 * @Version 1.0
 */
@Service
public class AutoConfigServiceImpl implements AutoConfigService {
    private static final Logger logger = LoggerFactory.getLogger(AutoConfigServiceImpl.class);

    @Autowired
    private AutoConfigEntity autoConfigEntity;

    @Override
    public void print() {
        logger.info("---------------- 默认： AutoConfigService print()--------------");
        logger.info("默认初始化后,autoConfigEntity:" + JSON.toJSONString(autoConfigEntity));
    }
}
