package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: zhangj
 * @Date: 2019-11-28
 * @Version 1.0
 */

@SpringBootApplication
public class SSeImproApplication {
    private static final Logger logger = LoggerFactory.getLogger(SSeImproApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SSeImproApplication.class, args);
    }
}
