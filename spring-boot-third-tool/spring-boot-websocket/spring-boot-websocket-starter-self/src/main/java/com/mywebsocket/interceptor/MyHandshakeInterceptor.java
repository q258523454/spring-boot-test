package com.mywebsocket.interceptor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Configuration
public class MyHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * 在握手之前调用
     * 握手前执行（返回true才能建立连接）
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("interceptor: before handshake");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            // 获取请求中的参数或头信息，例如用户ID
            String userId = httpServletRequest.getParameter("userId");
            if (userId != null) {
                // 将用户ID添加到会话属性中
                attributes.put("userId", userId);
                log.info("interceptor: userId:{}", userId);
            }
        } else {
            // TODO
            log.warn("not handshake request");
        }
        // 建立握手
        return true;
    }

    /**
     * 在握手之后调用
     * 可以用于记录日志或其他操作
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("interceptor: after handshake");
    }
}