package com.mywebsocket.confg;

import com.mywebsocket.handler.MyWebSocketHandler;
import com.mywebsocket.interceptor.MyHandshakeInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @Autowired
    private MyHandshakeInterceptor handshakeInterceptor;

    /**
     * 设置处理器、拦截器
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 设置处理器
        WebSocketHandlerRegistration registration = registry.addHandler(webSocketHandler, "/ws");
        // 设置拦截器
        registration.addInterceptors(handshakeInterceptor)
                // 允许所有域访问
                .setAllowedOrigins("*");
    }


    /**
     * 配置WebSocket容器参数
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // 用于设置文本消息的最大缓冲区大小，单位是字节。消息超过这个大小时，可能会导致消息被截断或者抛出异常。默认值：8192 字节（即 8KB）
        container.setMaxTextMessageBufferSize(8192);
        // 二进制消息的最大缓冲区大小，单位是字节。默认值同上
        container.setMaxBinaryMessageBufferSize(8192);
        // WebSocket 会话的最大空闲超时时间，单位是毫秒。默认值：60000 毫秒（即 1 分钟）。
        container.setMaxSessionIdleTimeout(600 * 1000L);
        // 异步发送消息的超时时间，单位是毫秒。如果在指定的时间内消息没有成功发送，会触发相应的超时处理。 默认值：10000 毫秒（即 10 秒）
        container.setAsyncSendTimeout(10 * 1000L);
        return container;
    }

}