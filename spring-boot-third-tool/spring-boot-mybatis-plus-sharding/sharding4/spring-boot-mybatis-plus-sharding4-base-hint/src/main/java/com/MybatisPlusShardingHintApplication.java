package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.shardinghint.dao"})
public class MybatisPlusShardingHintApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusShardingHintApplication.class, args);
    }

}