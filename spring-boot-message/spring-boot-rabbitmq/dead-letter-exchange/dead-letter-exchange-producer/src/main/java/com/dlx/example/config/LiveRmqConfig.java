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

/**
 * 队列与交换机定义与绑定
 * 消息会变成死信消息的场景：
 *
 * 1.拒签:
 * 消息被(basic.reject() or basic.nack()) and requeue = false，即消息被消费者拒绝签收，
 * 并且重新入队为false。有一种场景需要注意下：消费者设置了自动ACK，当重复投递次数达到了设置的
 * 最大retry次数之后，消息也会投递到死信队列，但是内部的原理还是调用了nack/reject。
 *
 * 2.TTL过期
 * 消息过期，过了TTL存活时间。
 *
 * 3.队列最大数
 * 队列设置了x-max-length最大消息数量且当前队列中的消息已经达到了这个数量，再次投递，消息将被挤掉，
 * drop-head(默认): 丢弃先到消息(FIFO), 挤出的先到消息会转到DLX(如果有死信队列)
 * reject-publish: 当队列消息满了时,拒绝后续消息(注意是直接reject,不会再转到死信DLX).
 */
@Data
@Slf4j
@Configuration
public class LiveRmqConfig {

    /**
     * queue-live
     */
    @Value("${rmq.queue.live}")
    private String liveQueueName;

    /**
     * exchange-live
     */
    @Value("${rmq.exchange.live}")
    private String liveExchangeName;

    /**
     * live.*
     */
    @Value("${rmq.route.live}")
    private String liveRouteKey;

    /**
     * 死信交换机和死信队列
     */
    @Autowired
    private DeadRmqConfig deadRmqConfig;

    /**
     * 给死信交换机传递的路由key, 死信交换机会根据该key来找到对应的死信队列
     */
    private static final String DEAD_ROUTE_KEY = "dead.live";


    @PostConstruct
    public void init() {
        log.info("初始化——live队列");
        log.info("初始化live队列,交换机信息:{}", JSON.toJSONString(liveExchange()));
        log.info("初始化live队列,队列信息:{}", JSON.toJSONString(liveQueue()));
    }


    /**
     * 正常交换机
     */
    @Bean
    public Exchange liveExchange() {
        // new TopicExchange(TOPIC_QUEUE)默认参数: durable:true,autoDelete:false
        return new TopicExchange(liveExchangeName, true, false);
    }


    /**
     * 正常队列 - 关联死信交换机
     * 队列添加了这个参数之后会自动与该交换机绑定，并设置路由键，不需要开发者手动设置
     * -------------- Queue 参数设置 -------------------
     * args 参数:
     *      队列60s没有被使用，则删除该队列
     *      args.put("x-expires", 60 * 1000);
     *      设置队列超时时间TTL,（可以在sender时对每条消息设置TTL）
     *      args.put("x-message-ttl", 3600 * 1000);
     *      队列中消息总共不能超过1000字节
     *      args.put("x-max-length-bytes", 1000);
     *      队列中最大的优先级等级为10（可以在sender时对每条消息设置优先级）
     *      args.put("x-max-priority", 10);
     *      结合"x-max-length"和'x-max-length'组合使用
     *      drop-head(默认): 丢弃先到消息(FIFO), 挤出的先到消息会转到DLX(如果有死信队列)
     *      reject-publish: 当队列消息满了时,拒绝后续消息(注意是直接reject,不会再转到死信DLX).
     *      args.put("x-overflow", "drop-head");
     *      args.put("x-overflow", "reject-publish");
     *      设置队列最大长度,超过3个就根据"x-overflow"策略来处理
     *      args.put("x-max-length", 3);
     * Queue 构造参数:
     *      durable:队列是否持久化.
     *              false:队列在内存中,服务器挂掉后,队列就没了;
     *              true:服务器重启后,队列将会重新生成.
     *              注意:只是队列持久化,不代表队列中的消息持久化
     *      exclusive:队列是否专属,专属的范围针对的是连接,也就是说,一个连接下面的多个信道是可见的.
     *                对于其他连接是不可见的.连接断开后,该队列会被删除.
     *                注意,不是信道断开,是连接断开.并且,就算设置成了持久化,也会删除.
     *      autoDelete: 如果所有消费者都断开连接了,是否自动删除.
     *                  如果还没有消费者从该队列获取过消息或者监听该队列,那么该队列不会删除.
     *                  只有在有消费者从该队列获取过消息后,该队列才有可能自动删除(当所有消费者都断开连接,不管消息是否获取完也会删除)
     *      new Queue(TOPIC_QUEUE)默认参数: durable:true, exclusive:false, autoDelete:false
     * ----------------------------------------------------
     */
    @Bean
    public Queue liveQueue() {
        Map<String, Object> args = new HashMap<>();
        // 声明了队列里的死信转发到的DLX名称，
        args.put("x-dead-letter-exchange", deadRmqConfig.getDeadExchangeName());
        // 指定死信消息在死信交换机中转发时携带的路由key
        args.put("x-dead-letter-routing-key", DEAD_ROUTE_KEY);
        return new Queue(liveQueueName, true, false, false, args);
    }

    /**
     * 正常交换机与正常队列绑定
     */
    @Bean
    public Binding liveBinding() {
        return BindingBuilder.bind(liveQueue()).to(liveExchange()).with(liveRouteKey).noargs();
    }


}