package com.demo.service.kafkaproduce;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
public class KafkaProducerService {

    /**
     * 生产者发送的topic
     */
    public static final String TOPIC = "demo-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送消息
     */
    public void sendMessage(String message) {
        log.info("发送消息: " + message);
        kafkaTemplate.send(TOPIC, message);
    }

    public void sendMessage(String topic, String message) {
        log.info("发送消息: " + message);
        kafkaTemplate.send(topic, message);
    }

    /**
     * 发送消息到Kafka, 带回调
     */
    public void sendMessageWithCallBack(String message) {
        sendMessageWithCallBack(TOPIC, message);
    }

    public void sendMessageWithCallBack(String topic, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        // 添加发送结果回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                RecordMetadata meta = result.getRecordMetadata();
                log.info("send success,message value: {}, topic is {}, partition is {}, offset is {}", result.getProducerRecord().value(),
                        meta.topic(), meta.partition(), meta.offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("send error: {}", ex.getMessage());
            }
        });
        log.info("future.isDone={}", future.isDone());
    }
}