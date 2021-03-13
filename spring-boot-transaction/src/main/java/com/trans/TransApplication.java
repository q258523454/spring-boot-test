package com.trans;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.trans.dao"})
public class TransApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransApplication.class, args);
    }

}