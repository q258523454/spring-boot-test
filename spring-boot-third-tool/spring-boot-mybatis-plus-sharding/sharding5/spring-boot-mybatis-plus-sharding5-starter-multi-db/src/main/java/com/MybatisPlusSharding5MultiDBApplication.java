package com;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.sharding5multidb.dao"})
public class MybatisPlusSharding5MultiDBApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusSharding5MultiDBApplication.class, args);
    }
}