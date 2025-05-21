package com.inter.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @date 2020-07-31 14:20
 * @modify
 */
@Configuration
@ConfigurationProperties(prefix = "dynamic-scheduled-config")
@Data
public class DynamicConfig {
    private String beanName;

    private String cron;

    private String method;
}
