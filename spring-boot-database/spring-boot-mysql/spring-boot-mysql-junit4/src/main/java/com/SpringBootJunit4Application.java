package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.junit.dao") // mybatis-dao层
public class SpringBootJunit4Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJunit4Application.class, args);
    }

    // war
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootJunit4Application.class);
    }
}


