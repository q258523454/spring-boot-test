package com.demo.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${my.kafka.bootstrap-servers}")
    private String servers;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 当使用手动提交时必须设置ackMode为MANUAL,否则会报错
        // No Acknowledgment available as an argument, the listener container must have a MANUAL AckMode to populate the Acknowledgment.
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory);
        // 设置并发消费者数量（根据分区数调整）, @KafkaListener 会覆盖该值
        // factory.setConcurrency(3);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(KafkaProperties properties) {
        // 读取本地已经在yaml的kafka配置, 选择性覆盖
        Map<String, Object> localConsumerYaml = properties.buildConsumerProperties();
        Map<String, Object> configProps = new HashMap<>(localConsumerYaml);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // bootstrap.servers 服务地址
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        // 是否自动提交消费
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // session.timeout.ms, Kafka 通过 心跳机制 检测消费者是否存活, 否则触发 rebalance 重新分配分区
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        // request.timeout.ms 请求超时时间（poll消息或ack消息）
        configProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);
        // max.poll.interval.ms 两次调用poll()方法的最大间隔时间,超过Broker则认为pod处理太慢,触发Rebalance
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        // max.poll.records: 每次poll的最大记录数
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        /*
         * latest(默认):	 若没有提交过offset，则只消费消费者连接topic后新产生的数据, pod重启,数据可能会丢失
         * earliest: 若没有提交过offset，则从头消费没有ack的数据
         * none: 强制校验偏移量,若没有有效偏移量，消费者会启动失败并抛出异常：NoOffsetForPartitionException
         */
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 显式覆盖配置参数
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        // 配置SASL
        KafkaSASLConfig.configureSASL(configProps);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }
}