package com.annotation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("asyncLogUtil")
public class AsyncLogUtil {
    private static Logger logger = LoggerFactory.getLogger(AsyncLogUtil.class);

    @Async
    public void recordLog() {
        logger.info("-------------- operate log --------------");
    }
}
