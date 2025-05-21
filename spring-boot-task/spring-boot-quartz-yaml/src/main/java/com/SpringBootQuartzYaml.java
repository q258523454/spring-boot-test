package com;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


// quartz 数据源只是用来做定时任务的, 如果需要其他多数据源务必加下面注解 (单数据源不需要禁用)
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // 禁用掉默认的数据源获取方式，默认会读取配置文件的据源（spring.datasource.*）
@SpringBootApplication
@MapperScan("com.quartz.dao") // mybatis-dao层
public class SpringBootQuartzYaml extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootQuartzYaml.class, args);
    }
}



