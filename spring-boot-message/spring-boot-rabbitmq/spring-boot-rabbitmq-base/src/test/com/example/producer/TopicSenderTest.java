package com.example.producer;

import com.example.config.TopicConfig;
import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicSenderTest {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendTopic() {
        User user1 = new User("1", "msg1");
        User user2 = new User("2", "msg2");
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