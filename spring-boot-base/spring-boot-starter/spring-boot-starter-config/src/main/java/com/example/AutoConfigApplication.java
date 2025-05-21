package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;



@SpringBootApplication // 包含了注解 @EnableAutoConfiguration——自动加载配置 spring.factories/spring-autoconfigure-metadata.properties 启动bean
@EnableAsync
public class AutoConfigApplication {
    // 注意加入: spring-boot-auto-config/src/main/resources/META-INF/spring.factories
    public static void main(String[] args) {
        SpringApplication.run(AutoConfigApplication.class, args);
    }

}