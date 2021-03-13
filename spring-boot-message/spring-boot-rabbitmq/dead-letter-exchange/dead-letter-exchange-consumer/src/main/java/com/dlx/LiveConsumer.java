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
public class LiveConsumer {

    /**
     * 当consumer消费端成功消费完消息后，返回给broker确认通知，告诉broker移除队列中已经消费成功的消息，
     * 如果消费端消费失败，可以通知broker将消费失败的消息重新放回队列中，以便继续消费。
     *
     * channel.basicAck(deliveryTag, multiple);
     * consumer处理成功后，通知broker删除队列中的消息，如果设置multiple=true，表示支持批量确认机制以减少网络流量。
     * 例如：有值为5,6,7,8 deliveryTag的投递
     * 如果此时channel.basicAck(8, true);则表示前面未确认的5,6,7投递也一起确认处理完毕(批量确认)。
     * 如果此时channel.basicAck(8, false);则仅表示deliveryTag=8的消息已经成功处理,直接确认删除
     *
     * channel.basicNack(deliveryTag, multiple, requeue);
     * consumer处理失败后，例如：有值为5,6,7,8 deliveryTag的投递。
     * 如果channel.basicNack(8, true, true);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息重新放回队列中。
     * 如果channel.basicNack(8, true, false);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息直接丢弃。
     * 如果channel.basicNack(8, false, true);表示deliveryTag=8的消息处理失败且将该消息重新放回队列。
     * 如果channel.basicNack(8, false, false);表示deliveryTag=8的消息处理失败且将该消息直接丢弃。
     */
    @RabbitListener(queues = "${rmq.queue.live}")
    public void liveConsumer(String msg, Message message, Channel channel) {
        log.info("业务监听：接收到消息:[{}]", msg);
        // 投递序号(不管prefetch是多少,对每个消费线程而言,每一条新的消息都会将deliveryTag+1)
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 首次投递, 非二次投递
            if (!message.getMessageProperties().isRedelivered()) {
                log.info("首次消费, deliveryTag:{},尝试重试.", deliveryTag);
                // 处理失败,拒绝签收, requeue:false 表示直接丢弃 , true 表示重新投递
                channel.basicNack(deliveryTag, false, true);
            } else {
                log.info("开始重复消费");
                // TODO 做业务处理,如果失败则直接丢弃
                channel.basicNack(deliveryTag, false, false);

                MessageProperties messageProperties = message.getMessageProperties();
                String receivedRoutingKey = messageProperties.getReceivedRoutingKey();
                String receivedExchange = messageProperties.getReceivedExchange();
                String consumerQueue = messageProperties.getConsumerQueue();
                log.info(" 消息-{deliveryTag:{},exchange:{},route:{},queue:{}},消费失败,丢弃(转DLX).", deliveryTag, receivedExchange, receivedRoutingKey, consumerQueue);
            }
        } catch (IOException e) {
            log.error("消息处理失败", e);
            e.printStackTrace();
        }
    }
}
