package com.jar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @date 2020-08-17 18:52
 * @modify
 */
@Slf4j
@Configuration("beanConfig")
public class BeanConfig {

    private String beanName = "bean1";

    @PostConstruct
    public void init() {
        log.info("初始化了:" + this.getClass().getSimpleName());
    }
}
