package com.ssepro.controller;

import com.ssepro.service.SseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PreDestroy;


@Slf4j
@RestController
@RequestMapping("/ssepro")
public class SseController {

    @Autowired
    private SseService sseService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * postman不支持流式,用浏览器访问
     */
    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam("clientId") String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("clientId cannot be null or empty");
        }
        // 连接时间, 超过timeout则会释放连接. 注意:每次send()不会刷新时间.
        long timeout = 600 * 1000L;
        SseEmitter emitter = sseService.createEmitter(clientId, timeout);
        int clientCount = SseService.getClientCount();
        log.info("current client count:{}", clientCount);
        return emitter;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendToClient(@RequestParam("clientId") String clientId) {
        log.info("send to client clientId={}", clientId);
        String message = "I'm sending to " + clientId;
        sseService.sendToClient(clientId, message);
        return ResponseEntity.ok("send to client");
    }

    @PostMapping("/sendAll")
    public ResponseEntity<String> sendAll() {
        String message = "sseService send all";
        sseService.sendAll(message);
        return ResponseEntity.ok("sendAll");
    }

    @PostMapping("/sendAll2")
    public ResponseEntity<String> sendAll2() {
        log.info("applicationContext sendAll send all");
        String message = "applicationContext sendAll,hello world.";
        applicationContext.publishEvent(message);
        return ResponseEntity.ok("applicationContext sendAll,hello world.");
    }


    @DeleteMapping("/close")
    public ResponseEntity<String> closeConnection(@RequestParam("clientId") String clientId) {
        log.info("close client clientId={}", clientId);
        sseService.closeClient(clientId);
        int clientCount = SseService.getClientCount();
        log.info("close client clientId={} finished, client count:{}", clientId, clientCount);
        return ResponseEntity.ok("Close request received");
    }

    @PreDestroy
    public void destroy() {
        sseService.closeAll();
    }
}