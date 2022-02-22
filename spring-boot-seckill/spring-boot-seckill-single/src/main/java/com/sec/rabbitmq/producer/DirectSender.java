package com.sec.rabbitmq.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sec.pojo.dto.SeckillMessage;
import com.sec.rabbitmq.config.DirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(SeckillMessage message) {
        // 序列化
        ObjectMapper mapper = new ObjectMapper();
        String msg;
        try {
            msg = mapper.writeValueAsString(message);
        } catch (Exception e) {
            log.info("MQ message convert to json wrong");
            return;
        }

        //第一个队列是指发送到哪个队列，第二个是指发送的内容
        this.amqpTemplate.convertAndSend(DirectConfig.QUEUE_DIRECT, msg);
        log.info("MQ send message:" + msg);
    }
}