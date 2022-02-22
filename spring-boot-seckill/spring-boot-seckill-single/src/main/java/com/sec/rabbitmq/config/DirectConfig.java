package com.sec.rabbitmq.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {

    /**
     * 直连模式 queue
     */
    public static final String QUEUE_DIRECT = "seckill-direct";


    /**
     * Direct 模式
     */
    @Bean
    public Queue directQueue() {
        // 第一个参数是队列的名字，第二个参数是指是否持久化
        return new Queue(QUEUE_DIRECT, true);
    }
}
