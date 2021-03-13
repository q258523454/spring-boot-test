package com.logback.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @date 2020-04-20 22:06
 * @modify
 */
@RestController
public class MybatisLog {
    private static Logger logger = LoggerFactory.getLogger(MybatisLog.class);

    @PostConstruct
    public void init() {
        logger.debug("-------------- Mybatis 打印 debug ------------");
        logger.info("-------------- Mybatis 打印  info ------------");
        logger.warn("-------------- Mybatis 打印 warn ------------");
        logger.error("-------------- Mybatis 打印 error ------------");
    }
}
