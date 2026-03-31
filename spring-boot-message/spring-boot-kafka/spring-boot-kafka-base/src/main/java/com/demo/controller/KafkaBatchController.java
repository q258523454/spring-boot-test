package com.demo.controller;

import com.demo.service.kafkaproduce.KafkaBatchProducerService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RestController
@RequestMapping("/api/kafka/batch")
public class KafkaBatchController {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final KafkaBatchProducerService producerBatchService;

    private final BeanNameUrlHandlerMapping beanNameUrlHandlerMapping;

    @Autowired
    public KafkaBatchController(KafkaBatchProducerService producerService, BeanNameUrlHandlerMapping beanNameUrlHandlerMapping) {
        this.producerBatchService = producerService;
        this.beanNameUrlHandlerMapping = beanNameUrlHandlerMapping;
    }

    /**
     * 异步批量发送(带回调)
     */
    @GetMapping("/send/callback")
    public ResponseEntity<String> sendMessageWithCallBack() {
        for (int i = 0; i < 5; i++) {
            producerBatchService.sendMessageWithCallBack(COUNTER.incrementAndGet() + "");
        }
        log.info("/send/callback/batch 已执行");
        return ResponseEntity.ok("消息已发送");
    }
}