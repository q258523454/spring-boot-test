package com.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhangj
 * @Date: 2019-09-09
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan({"com.batch.dao"})
public class OracleBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(OracleBatchApplication.class, args);
    }

}