package com.oracle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhangj
 * @Date: 2019-09-09
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan({"com.oracle.vs.mybatis.dao"})
public class OracleVsJDBCApplication {
    public static void main(String[] args) {
        SpringApplication.run(OracleVsJDBCApplication.class, args);
    }

}