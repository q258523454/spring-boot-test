package com.logback.mylog;

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
public class MyLog {
    private static Logger logger = LoggerFactory.getLogger(MyLog.class);

    @PostConstruct
    public void init() {
        logger.debug("-------------- MyLog 打印 debug ------------");
        logger.info("-------------- MyLog 打印  info ------------");
        logger.warn("-------------- MyLog 打印 warn ------------");
        logger.error("-------------- MyLog 打印 error ------------");
    }
}
