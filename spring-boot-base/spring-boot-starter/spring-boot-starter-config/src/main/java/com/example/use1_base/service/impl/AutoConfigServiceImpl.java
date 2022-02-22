package com.example.use1_base.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.use1_base.config.MyAutoProperties;
import com.example.use1_base.service.AutoConfigService;
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
    private MyAutoProperties myAutoProperties;

    @Override
    public void  print() {
        logger.info("---------------- 初始化 AutoConfigService print()--------------");
        logger.info("初始化 myAutoProperties:" + JSON.toJSONString(myAutoProperties));
    }
}
