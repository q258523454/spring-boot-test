package com.aysnc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: zhangj
 * @Date: 2019-09-09
 * @Version 1.0
 */
/*
scanBasePackageClasses 可以帮助我们引入(扫描)其他jar中的bean
 */
@SpringBootApplication
@EnableAsync
public class AsyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }

}