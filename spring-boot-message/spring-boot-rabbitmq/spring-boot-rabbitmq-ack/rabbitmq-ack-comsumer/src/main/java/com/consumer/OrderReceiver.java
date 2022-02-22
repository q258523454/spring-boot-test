package com.consumer;

import com.ack.common.entity.Order;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderReceiver {


    /**
     * 当consumer消费端成功消费完消息后，返回给broker确认通知，告诉broker移除队列中已经消费成功的消息，
     * 如果消费端消费失败，可以通知broker将消费失败的消息重新放回队列中，以便继续消费。
     * channel.basicAck(deliveryTag, multiple);
     * consumer处理成功后，通知broker删除队列中的消息，如果设置multiple=true，表示支持批量确认机制以减少网络流量。
     * 例如：有值为5,6,7,8 deliveryTag的投递
     * 如果此时channel.basicAck(8, true);则表示前面未确认的5,6,7投递也一起确认处理完毕(批量确认)。
     * 如果此时channel.basicAck(8, false);则仅表示deliveryTag=8的消息已经成功处理,直接确认删除
     * channel.basicNack(deliveryTag, multiple, requeue);
     * consumer处理失败后，例如：有值为5,6,7,8 deliveryTag的投递。
     * 如果channel.basicNack(8, true, true);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息重新放回队列中。
     * 如果channel.basicNack(8, true, false);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息直接丢弃。
     * 如果channel.basicNack(8, false, true);表示deliveryTag=8的消息处理失败且将该消息重新放回队列。
     * 如果channel.basicNack(8, false, false);表示deliveryTag=8的消息处理失败且将该消息直接丢弃。
     */
    @RabbitListener(
            // 配置监听的哪一个队列，同时在没有queue和exchange的情况下会去创建并建立绑定关系
            bindings = @QueueBinding(
                    value = @Queue(value = "order.queue", durable = "true"),
                    exchange = @Exchange(name = "order-exchange", durable = "true", type = "topic"),
                    key = "order.*"
            )
    )
    @RabbitHandler
    public void onOrderMessage(Order order, Message message, Channel channel) throws Exception {
        log.info("----收到消息，开始消费-----");
        log.info("订单id：" + order.getId());

        /**
         * Delivery Tag 用来标识信道中投递的消息。RabbitMQ 推送消息给 Consumer 时，会附带一个 Delivery Tag，
         * 以便 Consumer 可以在消息确认时告诉 RabbitMQ 到底是哪条消息被确认了。
         * RabbitMQ 保证在每个信道中，每条消息的 Delivery Tag 从 1 开始递增。
         */
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        /**
         * 消息已经成功处理,直接确认删除
         */
        channel.basicAck(deliveryTag, false);
    }

}
