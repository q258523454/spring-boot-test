package com.dlx.example.sender;

import com.dlx.example.config.DelayRmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DelaySender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DelayRmqConfig delayRmqConfig;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public void send() {
        int i = atomicInteger.addAndGet(1);
        String msg = "delay-" + i;
        rabbitTemplate.convertAndSend(delayRmqConfig.getDelayExchangeName(), "delay.test", msg, message -> {
            // 设置消息的TTL超时时间,超时发送到死信队列(该队列不要配置消费者,这样可以达到延迟处理的效果)
            MessageProperties messageProperties = message.getMessageProperties();
            // 延迟10秒后发送到死信队列处理
            messageProperties.setExpiration(10 * 1000 + "");
            return message;
        });
        log.info("delay 发送完毕.msg:{}", msg);

    }
}