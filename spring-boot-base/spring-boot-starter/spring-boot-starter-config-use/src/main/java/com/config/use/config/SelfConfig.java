package com.config.use.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Slf4j
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
public class SelfConfig {
    private String msg;

    public SelfConfig() {
        log.info("---------------- 初始化自有 Config -------------------");
    }
}
