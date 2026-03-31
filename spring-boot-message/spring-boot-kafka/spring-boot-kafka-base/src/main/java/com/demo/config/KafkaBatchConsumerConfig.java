package com.demo.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
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
public class KafkaBatchConsumerConfig {

    @Value("${my.kafka.bootstrap-servers}")
    private String servers;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaBatchFactory(ConsumerFactory<String, String> batchConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        /*
         * MANUAL：单次ack.acknowledge()不会立马提交, 下一次 poll() 时才会一次性批量提交,性能更好
         * 注意: 如果在处理过程中某条消息失败，未调用 ack.acknowledge()，整个批次的 offset 不会提交，Kafka 会重新投递该批次消息。
         *      批量ACK时机: ack多少次后批量提交,取决于缓存的消息条数offset以及kafka的缓存时机
         *
         * MANUAL_IMMEDIATE：单次ack.acknowledge()立即提交偏移量,性能稍低
         * 注意: MANUAL_IMMEDIATE 不是指单条提交,而是单次ack立马提交, 可能是单条也可能是batch批量
         *      batch模式下一样适用,建议使用 MANUAL_IMMEDIATE, 既能满足批量提交, 也可以自行在批量消费中完成单次提交
         */
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(batchConsumerFactory);
        // 设置并发消费者数量（根据分区数调整）, @KafkaListener 会覆盖该值
        // factory.setConcurrency(3);
        // 支持 批量消费
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> batchConsumerFactory(KafkaProperties properties) {
        // 读取本地已经在yaml的kafka配置
        Map<String, Object> localYamlProperties = properties.buildConsumerProperties();
        Map<String, Object> configProps = new HashMap<>(localYamlProperties);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // bootstrap.servers 服务地址
        // configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "xxx:9093,xxx:9093");
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