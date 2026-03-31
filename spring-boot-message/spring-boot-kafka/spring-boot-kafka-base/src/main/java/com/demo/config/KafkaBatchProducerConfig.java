package com.demo.config;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaBatchProducerConfig {
    @Value("${my.kafka.bootstrap-servers}")
    private String servers;

    @Bean
    public KafkaTemplate<String, String> batchKafkaTemplate(ProducerFactory<String, String> batchProducerFactory) {
        return new KafkaTemplate<>(batchProducerFactory);
    }

    @Bean
    public ProducerFactory<String, String> batchProducerFactory(KafkaProperties properties) {
        // 读取本地已经在yaml的kafka生产者配置, 选择性覆盖
        Map<String, Object> localProducerYaml = properties.buildProducerProperties();
        Map<String, Object> configProps = new HashMap<>(localProducerYaml);
        // bootstrap.servers 服务器地址
        // configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "xxx:9093,xxx:9093");
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 显式覆盖配置参数
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        // 配置SASL
        KafkaSASLConfig.configureSASL(configProps);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

}