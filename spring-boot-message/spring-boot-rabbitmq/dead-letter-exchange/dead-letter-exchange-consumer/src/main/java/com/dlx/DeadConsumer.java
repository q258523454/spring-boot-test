package com.dlx;


import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DeadConsumer {


    /**
     * 死信队列消息监听者(消费者)
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "${rmq.queue.dead}")
    public void userDeadLetterConsumer(String msg, Message message, Channel channel) {
        log.info("接收到死信消息:[{}]", JSON.toJSONString(msg));
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            MessageProperties messageProperties = message.getMessageProperties();
            // 投递序号(不管prefetch是多少,对每个消费线程而言,每一条新的消息都会将deliveryTag+1)
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            String receivedRoutingKey = messageProperties.getReceivedRoutingKey();
            String receivedExchange = messageProperties.getReceivedExchange();
            String consumerQueue = messageProperties.getConsumerQueue();
            log.info(" 死信消息-{deliveryTag[{}],exchange:[{}],route:[{}],queue:[{}]},签收", deliveryTag, receivedExchange, receivedRoutingKey, consumerQueue);
        } catch (IOException e) {
            log.error("死信队列-消息签收异常", e);
            e.printStackTrace();
        }
    }
}
