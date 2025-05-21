package com.dlx.example.sender;

import com.dlx.example.config.PrefetchRmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrefetchSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PrefetchRmqConfig prefetchRmqConfig;

    public void send(String msg) {
        rabbitTemplate.convertAndSend(prefetchRmqConfig.getPrefetchExchangeName(), "prefetch.test", msg, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            return message;
        });
        log.info("prefetch 发送完毕.msg:{}", msg);
    }
}