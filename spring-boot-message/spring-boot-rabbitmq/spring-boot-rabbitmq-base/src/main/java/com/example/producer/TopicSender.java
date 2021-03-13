package com.example.producer;

import com.example.config.TopicConfig;
import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendTopic() {
        User user1 = new User("1", "com.msg.zhang");
        User user2 = new User("2", "com.msg.zhang.xiaofan");
        // 第一个参数:TopicExchange名字
        // 第二个参数:Route-key
        // 第三个参数:要发送的内容
        // TOPIC_QUEUE1_ROUTE_KEY = "com.msg.#";
        // TOPIC_QUEUE2_ROUTE_KEY = "com.msg.*";
        this.amqpTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, "com.msg.zhang", user1);
        this.amqpTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, "com.msg.zhang.xiaofan", user2);
        log.info("[sendTopic 已发送消息]");
    }
}