package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.shardingstandard.dao"})
public class MybatisPlusShardingStandardApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusShardingStandardApplication.class, args);
    }

}