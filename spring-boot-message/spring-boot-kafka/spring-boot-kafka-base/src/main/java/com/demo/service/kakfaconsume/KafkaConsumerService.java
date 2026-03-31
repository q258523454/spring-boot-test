package com.demo.service.kakfaconsume;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {
    /**
     * 监听Kafka消息
     * groupId 若不存在,则自动创建,且自定绑定topic
     */
    @KafkaListener(topics = {"#{'${my.kafka.default-topic}'}"}, groupId = "demo-group", concurrency = "1")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("单条处理: key={}, value={}, partition={}, offset={}", record.key(), record.value(), record.partition(),
                record.offset());
        // 手动提交偏移量...
        ack.acknowledge();
        log.info("单条处理: key={}, value={},ACK ", record.key(), record.value());
    }
}