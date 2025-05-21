package com.example.consumer;

import com.example.config.DirectConfig;
import com.example.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class DirectConsumer {

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

    //queues是指要监听的队列名字
    @RabbitListener(queues = DirectConfig.QUEUE_DIRECT)
    public void receiverDirectQueue(String str, Message message, Channel channel) throws IOException {
        // Json反序列化
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(str, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("[--------------" + DirectConfig.QUEUE_DIRECT + ": 监听到的消息]" + str);
        /**
         * 消息已经成功处理,直接确认删除
         * 一般只允许消息重复投递一次.
         * 先判断是否首次消费,业务成功后 ack, 失败则重新投递
         * 如果非首次消费, 业务成功后ack, 失败则丢弃,如果有死信队列，会自动转到DLX
         * 如果不ack, mq中不会删除这个消息,启动会再次pull
         */
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
