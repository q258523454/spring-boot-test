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

@EnableAsync
@SpringBootApplication
public class SSe2Application {
    private static final Logger logger = LoggerFactory.getLogger(SSe2Application.class);

    public static void main(String[] args) {
        SpringApplication.run(SSe2Application.class, args);
    }
}
