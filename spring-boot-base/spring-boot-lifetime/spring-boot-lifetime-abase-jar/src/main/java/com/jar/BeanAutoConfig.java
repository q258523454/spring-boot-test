package com.jar;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @date 2020-08-17 19:07
 * @modify
 */

@Slf4j
@Configuration("beanAutoConfig")
@ConfigurationProperties(prefix = "lifetime")
@ConditionalOnProperty(prefix = "lifetime", value = "enable", havingValue = "true", matchIfMissing = true)
@Data
public class BeanAutoConfig {
    private boolean enable = false;

    private String name;

    @PostConstruct
    public void init() {
        log.info("初始化了:" + this.getClass().getSimpleName());
    }
}
