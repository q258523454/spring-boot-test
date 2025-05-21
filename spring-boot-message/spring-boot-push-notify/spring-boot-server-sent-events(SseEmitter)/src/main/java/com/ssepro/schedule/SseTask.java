package com.ssepro.schedule;

import com.ssepro.service.SseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Configuration
public class SseTask {

    @Autowired
    private SseService sseService;

    /**
     * 定时发送一次事件
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void sendEvent() {
        log.info("do task");
        String message = "Server time: " + LocalDateTime.now();
        Map<String, SseEmitter> emitters = sseService.getEmitters();
        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            SseEmitter emitter = entry.getValue();
            String clientId = entry.getKey();
            try {
                emitter.send(SseEmitter.event()
                        .name("message") // 可选：指定事件名称
                        .data(message));
            } catch (IOException e) {
                log.warn("Task failed to send event to client:{}", clientId);
                // 移除无效的连接
                emitters.remove(clientId);
            }
        }
    }
}
