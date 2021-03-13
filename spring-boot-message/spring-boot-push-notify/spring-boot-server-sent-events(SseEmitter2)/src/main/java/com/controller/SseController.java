package com.controller;

import com.alibaba.fastjson.JSON;
import com.listener.MyEvent;
import com.listener.MyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-11-28
 * @Version 1.0
 */

@RestController
public class SseController {
    private static final Logger logger = LoggerFactory.getLogger(SseController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MyListener listener2;

    public static int num = 0;

    @GetMapping("/connect")
    public SseEmitter connect(String id) {
        // 每此刷新连接都是一次连接(实际环境中是以一次有效会话来的session+loginSession)
        if (null == id || id.isEmpty()) {
            id = UUID.randomUUID().toString().substring(0, 8);
        }
        num++;
        logger.info("请求id:{}", id + "-" + num);

        // SseEmitter 设置超时时间60s
        SseEmitter sseEmitter = new SseEmitter(0L);
        listener2.addSseEmitter(id + "-" + num, sseEmitter);
        return sseEmitter;
    }


    @GetMapping("/pushAll")
    public String pushAll(HttpServletRequest request, String msg) {
        // 每次会话
        // String id = request.getSession().getId();
        // 每此请求url
        // String id = UUID.randomUUID().toString();
        MyEvent myEvent = new MyEvent(this, msg);
        // 触发事件
        applicationContext.publishEvent(myEvent);
        return JSON.toJSONString(myEvent);
    }
}
