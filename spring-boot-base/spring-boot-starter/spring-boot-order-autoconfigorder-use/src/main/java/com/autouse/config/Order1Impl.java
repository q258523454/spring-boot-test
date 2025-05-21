package com.autouse.config;

import com.autouse.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


/**
 * @Description
 * @date 2020-04-21 20:51
 * @modify
 */
@Service
@Order(2)
public class Order1Impl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(Order1Impl.class);

    @PostConstruct
    public void init() {
        logger.info("---------------- 初始化加载: Order1 Impl  init 级别:Order(2)-------------------");
    }

    @Override
    public void printBeanName() {
        logger.info("-------------- Order1 Impl ---------------");
    }
}
