package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.shardinginline.dao"})
public class MybatisPlusShardingInlineApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusShardingInlineApplication.class, args);
    }

}