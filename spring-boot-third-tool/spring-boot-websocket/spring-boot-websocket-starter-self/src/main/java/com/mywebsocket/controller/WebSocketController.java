package com.mywebsocket.controller;


import com.mywebsocket.handler.MyWebSocketHandler;
import com.mywebsocket.manager.SessionManager;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Slf4j
@RestController
public class WebSocketController {

    @GetMapping(value = "/websocket/sendall")
    public String sendall() throws IOException {
        String message = "hello world";
        log.info("send all message: {}", message);
        SessionManager.sendToAll(message);
        return "send all";
    }
}
