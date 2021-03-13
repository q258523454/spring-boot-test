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
@Order(1)
public class Order2Impl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(Order2Impl.class);

    @PostConstruct
    public void init() {
        logger.info("---------------- 初始化加载: Order2 Impl  init 级别:Order(1)-------------------");
    }

    @Override
    public void printBeanName() {
        logger.info("-------------- Order2 Impl ---------------");
    }
}
