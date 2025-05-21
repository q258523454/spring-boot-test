package com.oracle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan({"com.oracle.vs.mybatis.dao"})
public class OracleVsJDBCApplication {
    public static void main(String[] args) {
        SpringApplication.run(OracleVsJDBCApplication.class, args);
    }

}