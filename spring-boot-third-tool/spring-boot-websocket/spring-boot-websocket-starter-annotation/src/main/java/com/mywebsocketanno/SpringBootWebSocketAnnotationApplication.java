package com.mywebsocketanno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class SpringBootWebSocketAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebSocketAnnotationApplication.class, args);
    }
}
