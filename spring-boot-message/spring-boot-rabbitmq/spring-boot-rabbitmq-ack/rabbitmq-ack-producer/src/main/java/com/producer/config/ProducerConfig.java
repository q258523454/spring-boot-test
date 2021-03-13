package com.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ProducerConfig {

    /**
     * topic模式 queue
     */
    public static final String TOPIC_QUEUE = "order.queue";
    public static final String TOPIC_QUEUE_ROUTE_KEY = "order.*";
    public static final String TOPIC_EXCHANGE = "order-exchange";

    /**
     * Topic 模式
     */
    @Bean
    public Queue topicQueue() {
        //new Queue(TOPIC_QUEUE)默认参数: durable:true, exclusive:false, autoDelete:false
        return new Queue(TOPIC_QUEUE, true, false, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        // durable(true)是持久化操作, mq重启之后交换机还在
        // new TopicExchange(TOPIC_QUEUE)默认参数: durable:true,autoDelete:false
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    /**
     * “*”和“#”,用于做模糊匹配，其中“*”用于匹配一个单词，“#”用于匹配多个单词(可以是零个)
     * @return
     */
    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(TOPIC_QUEUE_ROUTE_KEY);
    }

}