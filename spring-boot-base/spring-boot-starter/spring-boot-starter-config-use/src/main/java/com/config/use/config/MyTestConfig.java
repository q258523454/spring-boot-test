package com.config.use.config;

import com.config.use.util.SpringContextHolder;
import com.example.config.AutoConfig;
import com.example.entity.SingleEntity;
import com.example.entity.SingleEntity2;
import com.example.entity.SingleEntity3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;


/**
 * @Description
 * @date 2020-04-02 9:50
 * @modify
 */

@Configuration
@DependsOn("springContextHolder")
@Slf4j
public class MyTestConfig {

    @Autowired(required = false)
    private AutoConfig mAutoConfig;

    @PostConstruct
    public void init() {
        try {
            AutoConfig bean = SpringContextHolder.getBean(AutoConfig.class);
        } catch (Exception ex) {
            log.warn("Cause:{}", ex.getMessage());
        }
        log.info("--------------------------- PostConstruct 内部配置 初始化 BEGIN ---------------------------");
        log.info("AutoConfig :{}", SpringContextHolder.getBean(AutoConfig.class) != null ? SpringContextHolder.getBean(AutoConfig.class).toString() : "null");
        log.info("SingleEntity :{}", SpringContextHolder.getBean(SingleEntity.class) != null ? SpringContextHolder.getBean(SingleEntity.class).toString() : "null");
        log.info("SingleEntity2 :{}", SpringContextHolder.getBean(SingleEntity2.class) != null ? SpringContextHolder.getBean(SingleEntity2.class).toString() : "null");
        log.info("SingleEntity3 :{}", SpringContextHolder.getBean(SingleEntity3.class) != null ? SpringContextHolder.getBean(SingleEntity3.class).toString() : "null");
        log.info("--------------------------- PostConstruct 内部配置 初始化 END ---------------------------");

    }
}
