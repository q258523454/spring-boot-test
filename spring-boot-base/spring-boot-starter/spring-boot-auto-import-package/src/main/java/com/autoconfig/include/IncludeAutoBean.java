package com.autoconfig.include;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created By
 *
 * @date :   2020-04-18
 */

@Component
public class IncludeAutoBean {
    private static Logger logger = LoggerFactory.getLogger(IncludeAutoBean.class);

    @PostConstruct
    public void init() {
        logger.info("-------------------------------------------------------------------");
        logger.info("--------------------- Auto IncludeBean @Componet 注入成功 ---------------------");
        logger.info("-------------------------------------------------------------------");
    }
}
