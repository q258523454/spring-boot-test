package com.mysql.level;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.mysql.level.dao"})
public class MybatisCacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisCacheApplication.class, args);
    }

}