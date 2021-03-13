package com.dlx.example.config;

import com.alibaba.fastjson.JSON;
import com.dlx.example.config.dead.DeadRmqConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Configuration
public class PrefetchRmqConfig {

    @Value("${rmq.queue.prefetch}")
    private String prefetchQueueName;

    @Value("${rmq.exchange.prefetch}")
    private String prefetchExchangeName;

    @Value("${rmq.route.prefetch}")
    private String prefetchRouteKey;

    /**
     * 死信交换机和死信队列
     */
    @Autowired
    private DeadRmqConfig deadRmqConfig;

    /**
     * 给死信交换机传递的路由key, 死信交换机会根据该key来找到对应的死信队列
     */
    private static final String DEAD_ROUTE_KEY = "dead.prefetch";


    @PostConstruct
    public void init() {
        log.info("初始化——prefetch队列");
        log.info("初始化prefetch队列,交换机信息:{}", JSON.toJSONString(prefetchExchange()));
        log.info("初始化prefetch队列,队列信息:{}", JSON.toJSONString(prefetchQueue()));
    }


    /**
     * prefetch交换机
     */
    @Bean
    public Exchange prefetchExchange() {
        // new TopicExchange(TOPIC_QUEUE)默认参数: durable:true,autoDelete:false
        return new TopicExchange(prefetchExchangeName, true, false);
    }


    /**
     * prefetch队列 - 关联死信交换机
     * 队列添加了这个参数之后会自动与该交换机绑定，并设置路由键，不需要开发者手动设置
     */
    @Bean
    public Queue prefetchQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", deadRmqConfig.getDeadExchangeName());
        args.put("x-dead-letter-routing-key", DEAD_ROUTE_KEY);
        return new Queue(prefetchQueueName, true, false, false, args);
    }

    /**
     * prefetch交换机与prefetch队列绑定
     */
    @Bean
    public Binding prefetchBinding() {
        return BindingBuilder.bind(prefetchQueue()).to(prefetchExchange()).with(prefetchRouteKey).noargs();
    }


}