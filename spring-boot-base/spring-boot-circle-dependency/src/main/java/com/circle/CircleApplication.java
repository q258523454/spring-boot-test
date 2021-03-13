package com.circle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.circle",
        })
public class CircleApplication {

    /**
     * 解决循环依赖:
     *  1.@Autowired
     *  2.setter方法
     * 报错:
     *  构造方法(单例模式报错)
     *
     */
    public static void main(String[] args) {
        SpringApplication.run(CircleApplication.class, args);
    }
}