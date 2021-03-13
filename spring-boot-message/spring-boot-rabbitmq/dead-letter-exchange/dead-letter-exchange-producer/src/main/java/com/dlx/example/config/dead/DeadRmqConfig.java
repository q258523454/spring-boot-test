package com.dlx.example.config.dead;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Configuration
public class DeadRmqConfig {

    /**
     * queue-dead
     */
    @Value("${rmq.exchange.dead}")
    private String deadExchangeName;

    /**
     * exchange-dead
     */
    @Value("${rmq.queue.dead}")
    private String deadQueueName;

    /**
     * dead.*
     */
    @Value("${rmq.route.dead}")
    private String deadRouteKey;

    @PostConstruct
    public void init() {
        log.info("初始化——死信队列");
        log.info("初始化死信队列,交换机信息:{}", JSON.toJSONString(deadExchange()));
        log.info("初始化死信队列,队列信息:{}", JSON.toJSONString(deadQueue()));
    }

    /**
     * 死信交换机
     */
    @Bean
    public Exchange deadExchange() {
        // new TopicExchange(TOPIC_QUEUE)默认参数: durable:true,autoDelete:false
        return new TopicExchange(deadExchangeName, true, false);
    }


    /**
     * 用户队列的死信消息 路由的队列
     * 用户队列user-queue的死信投递到死信交换机`common-dead-letter-exchange`后再投递到该队列
     * 用这个队列来接收user-queue的死信消息
     * @return
     */
    @Bean
    public Queue deadQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置超时时间TTL, 或者在发送的时候设置消息超时时间TTL, 默认死信队列保存7天
        args.put("x-message-ttl", 7 * 24 * 60 * 60 * 1000);
        return new Queue(deadQueueName, true, false, false, args);
    }

    /**
     * 死信交换机绑定队列
     */
    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange())
                .with(deadRouteKey)
                .noargs();
    }


}
