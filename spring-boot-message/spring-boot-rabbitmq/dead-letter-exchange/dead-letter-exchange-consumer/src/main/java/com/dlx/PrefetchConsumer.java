package com.dlx;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class PrefetchConsumer {

    /**
     * Prefetch队列消息监听者(消费者)
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(
            queues = "${rmq.queue.prefetch}",
            //RabbitListener开启几个线程去处理数据
            concurrency = "1")
    public void prefetchConsumer(String msg, Message message, Channel channel) throws IOException, InterruptedException {
        log.info("Prefetch监听：接收到消息:[{}]", msg);
        MessageProperties messageProperties = message.getMessageProperties();
        // 投递序号(不管prefetch是多少,对每个消费线程而言,每一条新的消息都会将deliveryTag+1),channel下唯一
        long deliveryTag = messageProperties.getDeliveryTag();
        String receivedRoutingKey = messageProperties.getReceivedRoutingKey();
        String receivedExchange = messageProperties.getReceivedExchange();
        String consumerQueue = messageProperties.getConsumerQueue();
        String threadName = Thread.currentThread().getName();
        // TODO 业务操作
        // 处理失败且将该消息重新放回队
        //channel.basicNack(deliveryTag, false, true);

        // 消息已经成功处理,直接确认删除
        channel.basicAck(deliveryTag, false);

        log.info(" [{}]消费[{}]成功-{deliveryTag:[{}],E:[{}],R:[{}],Q:[{}]}.", threadName, msg, deliveryTag, receivedExchange, receivedRoutingKey, consumerQueue);
    }

    /**
     * 下面的这个为了测试 prefetch 参数的意义
     * prefetch:n 就是指消息没有 ack 之前, 当前消费线程会取n条消息存到本地缓存区域
     * 例如：下面的线程会先拿n条消息放到缓存,然后依次消费.再没有消息ack之前,是不会再从mq获取消息的
     */
    @RabbitListener(
            queues = "${rmq.queue.prefetch}",
            //RabbitListener开启几个线程去处理数据
            concurrency = "1")
    public void prefetchConsumer2(String msg, Message message, Channel channel) throws IOException, InterruptedException {
        log.info("Prefetch2 监听：接收到消息:[{}]", msg);
        MessageProperties messageProperties = message.getMessageProperties();
        // 投递序号(不管prefetch是多少,对每个消费线程而言,每一条新的消息都会将deliveryTag+1),channel下唯一
        long deliveryTag = messageProperties.getDeliveryTag();
        String receivedRoutingKey = messageProperties.getReceivedRoutingKey();
        String receivedExchange = messageProperties.getReceivedExchange();
        String consumerQueue = messageProperties.getConsumerQueue();

        // 睡眠, 校验 prefetch 功能
        Thread.sleep(5000);

        // 消息已经成功处理,直接确认删除
        channel.basicAck(deliveryTag, false);

        log.info("Prefetch2 消费 [{}]成功-{deliveryTag:[{}],E:[{}],R:[{}],Q:[{}]}.", msg, deliveryTag, receivedExchange, receivedRoutingKey, consumerQueue);
    }
}
