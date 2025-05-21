package com.myjetcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan({"com.aop.dao"})
public class OracleBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(OracleBatchApplication.class, args);
    }

}