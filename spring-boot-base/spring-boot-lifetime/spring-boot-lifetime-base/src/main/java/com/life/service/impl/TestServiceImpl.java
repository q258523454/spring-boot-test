package com.life.service.impl;

import com.life.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: zhangj
 * @Date: 2019-09-10
 * @Version 1.0
 */
@Service("testSerivice")
@Slf4j
public class TestServiceImpl implements TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @Override
    public void hi() {
        log.info("myTestService");
    }
}
