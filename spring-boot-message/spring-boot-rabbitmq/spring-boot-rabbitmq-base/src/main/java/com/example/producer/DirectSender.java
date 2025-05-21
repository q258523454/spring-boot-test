package com.example.producer;

import com.example.config.DirectConfig;
import com.example.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendDirect() {
        User user = new User("123456", "zhangxiaofan");

        // 序列化
        ObjectMapper mapper = new ObjectMapper();
        String msg;
        try {
            msg = mapper.writeValueAsString(user);
        } catch (Exception e) {
            log.info("MqMessage convert to json wrong");
            return;
        }

        //第一个队列是指发送到哪个队列，第二个是指发送的内容
        this.amqpTemplate.convertAndSend(DirectConfig.QUEUE_DIRECT, msg);
        log.info("[sendDirectQueue 已发送消息]");
    }
}