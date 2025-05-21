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
     * #:匹配一个或多个单词
     * *:只匹配一个单词
     */
    public static final String TOPIC_QUEUE1 = "test.topic.queue.email";
    public static final String TOPIC_QUEUE1_ROUTE_KEY = "test.msg.#";

    public static final String TOPIC_QUEUE2 = "test.topic.queue.sms";
    public static final String TOPIC_QUEUE2_ROUTE_KEY = "test.msg.*";

    public static final String TOPIC_EXCHANGE = "test.exchange.topic";

    /**
     * Topic 模式
     */
    @Bean
    public Queue topicQueue1() {
        /**
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
         */
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
