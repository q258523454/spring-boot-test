package com.example.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    /**
     * topic模式 queue
     */
    public static final String TOPIC_QUEUE1 = "queue.email";
    public static final String TOPIC_QUEUE1_ROUTE_KEY = "com.msg.#";

    public static final String TOPIC_QUEUE2 = "queue.sms";
    public static final String TOPIC_QUEUE2_ROUTE_KEY = "com.msg.*";

    public static final String TOPIC_EXCHANGE = "exchange.topic.test";

    /**
     * Topic 模式
     */
    @Bean
    public Queue topicQueue1() {
        // 默认
        return new Queue(TOPIC_QUEUE1, true, false, false);
    }

    @Bean
    public Queue topicQueue2() {
        // 默认
        return new Queue(TOPIC_QUEUE2, true, false, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        // durable(true)是持久化操作, mq重启之后交换机还在,当消费者ack之后才会删除
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(TOPIC_QUEUE1_ROUTE_KEY);
    }

    /**
     * “*”和“#”,用于做模糊匹配，其中“*”用于匹配一个单词，“#”用于匹配多个单词(可以是零个)
     * @return
     */
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(TOPIC_QUEUE2_ROUTE_KEY);
    }
}
