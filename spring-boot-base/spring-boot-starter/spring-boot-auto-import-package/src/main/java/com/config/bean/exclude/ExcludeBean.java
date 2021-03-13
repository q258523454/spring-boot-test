package com.config.bean.exclude;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created By
 *
 * @date :   2020-04-18
 */

@Component
public class ExcludeBean {
    private static Logger logger = LoggerFactory.getLogger(ExcludeBean.class);

    @PostConstruct
    public void init() {
        logger.info("-------------------------------------------------------------------");
        logger.info("--------------------- ExcludeBean @Componet 注入成功 ---------------------");
        logger.info("-------------------------------------------------------------------");
    }
}
