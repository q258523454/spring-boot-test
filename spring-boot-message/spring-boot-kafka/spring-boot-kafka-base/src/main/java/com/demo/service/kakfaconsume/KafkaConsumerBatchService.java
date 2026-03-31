package com.demo.service.kakfaconsume;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerBatchService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerBatchService.class);

    /**
     * groupId 若不存在,则自动创建,且自定绑定topic
     */
    @KafkaListener(topics = {"#{'${my.kafka.default-batch-topic}'}"}, groupId = "demo-group", containerFactory = "kafkaBatchFactory", concurrency = "1")
    public void listen(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        logger.info("批量处理消息，数量: {}", records.size());
        try {
            // 处理批量消息
            for (ConsumerRecord<String, String> record : records) {
                processMessage(record);
            }

            // Batch模式结合 MANUAL_IMMEDIATE 自行设置提交偏移量
            // ackSelf(records, acknowledgment);

            // 手动提交偏移量
            acknowledgment.acknowledge();
            logger.info("批量消息处理完成，提交偏移量");
        } catch (Exception e) {
            logger.error("处理批量消息失败", e);
            // 这里可以根据业务需求实现重试或其他处理逻辑
        }
    }

    /**
     * Batch模式结合 MANUAL_IMMEDIATE 自行设置提交偏移量
     */
    private void ackSelf(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        for (int i = 0; i < records.size(); i++) {
            processMessage(records.get(i));
            if (i % 100 == 0) {
                acknowledgment.acknowledge(); // 每处理100条提交一次偏移量
            }
        }
    }

    // 处理单条消息的方法
    private void processMessage(ConsumerRecord<String, String> record) {
        try {
            logger.info("处理消息: key={}, value={}, partition={}, offset={}", record.key(), record.value(), record.partition(), record.offset());
            // 模拟业务处理
            Thread.sleep(100);
        } catch (Exception e) {
            logger.error("处理单条消息失败", e);
        }
    }
}    