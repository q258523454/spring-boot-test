package com.config.use;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhangj
 * @Date: 2019-09-09
 * @Version 1.0
 */
@SpringBootApplication(
        scanBasePackages = "com.config.use"
)
public class AutoConfigUseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigUseApplication.class, args);
    }
}