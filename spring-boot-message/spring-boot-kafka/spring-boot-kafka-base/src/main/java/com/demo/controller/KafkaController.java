package com.demo.controller;

import com.demo.service.kafkaproduce.KafkaProducerService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final KafkaProducerService producerService;

    private final BeanNameUrlHandlerMapping beanNameUrlHandlerMapping;

    @Autowired
    public KafkaController(KafkaProducerService producerService, BeanNameUrlHandlerMapping beanNameUrlHandlerMapping) {
        this.producerService = producerService;
        this.beanNameUrlHandlerMapping = beanNameUrlHandlerMapping;
    }

    /**
     * 同步发送
     */
    @GetMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam("msg") String message) {
        producerService.sendMessage(message);
        log.info("/send 已执行");
        return ResponseEntity.ok("消息已发送: " + message);
    }

    /**
     * 异步发送(带回调)
     */
    @GetMapping("/send/callback")
    public ResponseEntity<String> sendMessageWithCallBack() {
        for (int i = 0; i < 5; i++) {
            producerService.sendMessageWithCallBack(COUNTER.incrementAndGet() + "");
        }
        log.info("/send/callback/batch 已执行");
        return ResponseEntity.ok("消息已发送");
    }
}