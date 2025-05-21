package com.mywebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class SpringBootWebSocketSelfApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebSocketSelfApplication.class, args);
    }
}
