package com.jar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @date 2020-08-17 18:52
 * @modify
 */
@Slf4j
@Service("beanService")
public class BeanService {

    private String beanName = "bean1";

    @PostConstruct
    public void init() {
        log.info("初始化了:" + this.getClass().getSimpleName());
    }
}
