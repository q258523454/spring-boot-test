package com.dlx.example.sender;

import com.dlx.example.config.LiveRmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LiveSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LiveRmqConfig liveRmqConfig;

    public void send(String msg) {
        rabbitTemplate.convertAndSend(liveRmqConfig.getLiveExchangeName(), "live.test", msg, message -> {
            // 设置消息的TTL超时时间,如果没有被处理,超时后会发送到死信队列
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setExpiration(3600 * 1000 + "");
            return message;
        });
        log.info("live 发送完毕.msg:{}", msg);
    }
}