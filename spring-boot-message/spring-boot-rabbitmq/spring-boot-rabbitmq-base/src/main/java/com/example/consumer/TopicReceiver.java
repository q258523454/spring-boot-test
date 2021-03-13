package com.example.consumer;

import com.example.config.TopicConfig;
import com.example.entity.User;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TopicReceiver {
    //queues是指要监听的队列名字
    @RabbitListener(queues = TopicConfig.TOPIC_QUEUE1)
    public void receiverTopicQueue1(User user, Message message, Channel channel) {
        log.info("[--------------" + TopicConfig.TOPIC_QUEUE1 + "：监听到的消息]" + user.toString());
    }

    @RabbitListener(queues = TopicConfig.TOPIC_QUEUE2)
    public void receiverTopicQueue2(User user, Message message, Channel channel) {
        log.info("[--------------" + TopicConfig.TOPIC_QUEUE2 + "：监听到的消息]" + user.toString());
    }
}
