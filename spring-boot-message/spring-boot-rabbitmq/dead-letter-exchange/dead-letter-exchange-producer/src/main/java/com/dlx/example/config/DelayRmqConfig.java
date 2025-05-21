package com.dlx.example.config;

import com.alibaba.fastjson.JSON;
import com.dlx.example.config.dead.DeadRmqConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * 延迟队列
 * 延迟队列不设置消费之,只设置TTL时间,用来模拟延迟发送,
 * TTL到期后会发送到指定的死信交换机,交换机根据传递来的路由key来发送到对应的死信队列
 * 死信队列会被消费,到达延迟发送的目的
 */
@Data
@Slf4j
@Configuration
public class DelayRmqConfig {

    /**
     * queue-delay
     */
    @Value("${rmq.queue.delay}")
    private String delayQueueName;

    /**
     * exchange-delay
     */
    @Value("${rmq.exchange.delay}")
    private String delayExchangeName;

    /**
     * delay.*
     */
    @Value("${rmq.route.delay}")
    private String delayRouteKey;


    /**
     * 死信交换机和死信队列
     */
    @Autowired
    private DeadRmqConfig deadRmqConfig;

    /**
     * 给死信交换机传递的路由key, 死信交换机会根据该key来找到对应的死信队列
     */
    private static final String DEAD_ROUTE_KEY = "dead.delay";

    @PostConstruct
    public void init() {
        log.info("初始化——延迟队列");
        log.info("初始化延迟队列,交换机信息:{}", JSON.toJSONString(delayExchange()));
        log.info("初始化延迟队列,队列信息:{}", JSON.toJSONString(delayQueue()));
    }

    /**
     * 延迟交换机
     */
    @Bean
    public Exchange delayExchange() {
        // new TopicExchange(TOPIC_QUEUE)默认参数: durable:true,autoDelete:false
        return new TopicExchange(delayExchangeName, true, false);
    }

    /**
     * 延迟队列 - 关联死信交换机
     * 队列添加了这个参数之后会自动与该交换机绑定，并设置路由键，不需要开发者手动设置
     */
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", deadRmqConfig.getDeadExchangeName());
        args.put("x-dead-letter-routing-key", DEAD_ROUTE_KEY);
        // 设置队列最大长度,超过3个,则根据 x-overflow 的设置做不同的处理
        args.put("x-max-length", 3);
        // 结合'x-max-length'使用
        // drop-head(默认): 丢弃先到消息(FIFO), 挤出的先到消息会转到DLX(如果有死信队列)
        // reject-publish: 当队列消息满了时,拒绝后续消息(注意是直接reject,不会再转到死信DLX).
        args.put("x-overflow", "reject-publish");

        //new Queue(TOPIC_QUEUE)默认参数: durable:true, exclusive:false, autoDelete:false
        return new Queue(delayQueueName, true, false, false, args);
    }

    /**
     * 延迟交换机与延迟队列绑定
     */
    @Bean
    public Binding delayBinding(@Qualifier("delayExchange") Exchange exchange,
                                @Qualifier("delayQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(delayRouteKey).noargs();
        // 或者直接调用方法
        //return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(delayRouteKey).noargs();
    }

}