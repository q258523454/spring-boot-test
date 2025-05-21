package com.controller;

import com.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SubscribeController {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeController.class);

    @GetMapping("/subscribe")
    public void subscribe(HttpServletRequest req, HttpServletResponse res, @RequestParam("topic") String topic) {
        logger.info("收到订阅请求：topic:{}", topic);
        RequestUtils.addSubscrib(topic, req, res);
    }

    @GetMapping("/publish")
    public void publish(@RequestParam("topic") String topic, @RequestParam("content") String content) {
        logger.info("发布消息：topic:{}, content:{}", topic, content);
        RequestUtils.publishMessage(topic, content);
    }

}
