package com.aop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhangj
 * @Date: 2019-09-09
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan({"com.aop.dao"})
public class OracleBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(OracleBatchApplication.class, args);
    }

}