package com;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.sharding5starter.dao"})
public class MybatisPlusSharding5Application {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusSharding5Application.class, args);
    }
}