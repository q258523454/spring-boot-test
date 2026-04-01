package com.inter.config.bean.include.sub;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created By
 *
 * @date :   2020-04-18
 */

@Component
public class SubIncludeBean {
    private static Logger logger = LoggerFactory.getLogger(SubIncludeBean.class);

    @PostConstruct
    public void init() {
        logger.info("-------------------------------------------------------------------");
        logger.info("--------------------- SubIncludeBean @Componet 注入成功 ---------------------");
        logger.info("-------------------------------------------------------------------");
    }
}
