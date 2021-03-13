package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Console;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-11-28
 * @Version 1.0
 */

@Component
public class AsyncHelper {
    private static final Logger logger = LoggerFactory.getLogger(AsyncHelper.class);

    @Async
    public void sse(SseEmitter sseEmitter, int num, long time_sec) throws IOException {
        for (int i = 0; i < num; i++) {
            logger.info("{}:Sse sent begin", i);
            try {
                Thread.sleep(2000);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                Thread.currentThread().interrupt();
            }

            try {
                sseEmitter.send(SseEmitter.event().
                        comment("Sse Event").
                        id(UUID.randomUUID().toString())
                        .name("name")
                        .reconnectTime(3000)
                        .data("msg:" + i));
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                sseEmitter.completeWithError(ex);
            }

            logger.info("{}:Sse sent over", i);
        }
        sseEmitter.complete();

    }
}
