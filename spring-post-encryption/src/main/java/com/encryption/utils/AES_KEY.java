package com.encryption.utils;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-14
 */

@Component
@ConfigurationProperties(prefix = "global.aes.key")
public class AES_KEY {
    private Logger log = Logger.getLogger(this.getClass());

    private String handekeji;

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public String getHandekeji() {
        return handekeji;
    }

    public void setHandekeji(String handekeji) {
        this.handekeji = handekeji;
    }
}
