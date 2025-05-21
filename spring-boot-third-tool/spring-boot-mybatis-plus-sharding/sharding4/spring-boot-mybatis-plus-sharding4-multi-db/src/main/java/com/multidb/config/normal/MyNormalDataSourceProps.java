package com.multidb.config.normal;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MyNormalDataSourceProps {

    @Value("${spring.normal.datasource.username}")
    private String username;

    @Value("${spring.normal.datasource.password}")
    private String password;

    @Value("${spring.normal.datasource.url}")
    private String url;

    @Value("${spring.normal.datasource.driver-class-name}")
    private String driverClassName;

}
