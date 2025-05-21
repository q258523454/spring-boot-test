package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.inter.dao"})
public class MybatisPlusInterceptorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusInterceptorApplication.class, args);
    }

}