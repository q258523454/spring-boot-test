package com.inter.config.bean.include;

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
public class IncludeBean {
    private static Logger logger = LoggerFactory.getLogger(IncludeBean.class);

    @PostConstruct
    public void init() {
        logger.info("-------------------------------------------------------------------");
        logger.info("--------------------- IncludeBean @Componet 注入成功 ---------------------");
        logger.info("-------------------------------------------------------------------");
    }
}
