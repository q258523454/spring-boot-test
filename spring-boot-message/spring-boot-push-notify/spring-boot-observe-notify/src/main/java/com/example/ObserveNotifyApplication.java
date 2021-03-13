package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: zhangj
 * @Date: 2019-09-09
 * @Version 1.0
 */

@SpringBootApplication
public class ObserveNotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObserveNotifyApplication.class, args);
    }

}