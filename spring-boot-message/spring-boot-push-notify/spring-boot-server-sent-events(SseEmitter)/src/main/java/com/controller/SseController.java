package com.controller;

import com.util.AsyncHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @Author: zhangj
 * @Date: 2019-11-28
 * @Version 1.0
 */

@RestController
public class SseController {
    private static final Logger logger = LoggerFactory.getLogger(SseController.class);

    @Autowired
    private AsyncHelper asyncHelper;

    // 注意: 只支持 SseEmitter 只支持GET
    @GetMapping("/sse")
    public SseEmitter sseEmitter(int num) throws IOException {
        SseEmitter sseEmitter = new SseEmitter(0L); // 用不超时
        asyncHelper.sse(sseEmitter, num, 0);
        logger.info("api done");
        return sseEmitter;
    }
}
