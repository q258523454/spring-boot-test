package com.mywebsocketanno.controller;

import com.mywebsocketanno.manager.SessionManager;
import com.mywebsocketanno.handler.WebSocketHandlerServer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WebSocketController {

    @Autowired
    private WebSocketHandlerServer webSocketHandlerServer;

    @GetMapping(value = "/websocket/sendall")
    public String sendall() {
        String message = "hello world";
        log.info("send all message: {}", message);
        SessionManager.sendToAll(message);
        return "send all";
    }
}
