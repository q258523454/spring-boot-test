package com.annotation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @date 2020-04-16 9:49
 * @modify
 */

@Component
public class ExcludeTest {

    private static final Logger logger = LoggerFactory.getLogger(ExcludeTest.class);

    @PostConstruct
    public void init() {
        logger.info("---------------------- ExcludeTest init ----------------------");
    }
}
