package com.mywebsocket.manager;


import lombok.extern.slf4j.Slf4j;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public enum SessionManager {
    ;
    /**
     * 使用线程安全的Map存储会话
     */
    private static final ConcurrentHashMap<String, WebSocketSession> SESSIONS_MAP = new ConcurrentHashMap<>();

    /**
     * 会话数
     */
    private static final AtomicInteger SESSION_COUNT = new AtomicInteger(0);

    /**
     * 注册会话
     */
    public static void add(String userId, WebSocketSession session) {
        if (userId == null) {
            throw new NullPointerException("userId is null");
        }
        SESSIONS_MAP.put(userId, session);
    }

    /**
     * 获取会话
     */
    public static WebSocketSession getSession(String userId) {
        if (userId == null) {
            throw new NullPointerException("userId is null");
        }
        return SESSIONS_MAP.get(userId);
    }

    /**
     * 注销会话
     */
    public static void remove(String userId) {
        if (userId == null) {
            throw new NullPointerException("userId is null");
        }
        SESSIONS_MAP.remove(userId);
    }

    public static int getSessionCount() {
        return SESSION_COUNT.intValue();
    }

    public static void incrementSessionCount() {
        SESSION_COUNT.incrementAndGet();
    }

    public static void decrementSessionCount() {
        SESSION_COUNT.decrementAndGet();
    }

    /**
     * 发送消息给指定用户
     */
    public static void sendToUser(String userId, TextMessage message) {
        WebSocketSession session = SESSIONS_MAP.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                log.error("Message send failed:{}", e.getMessage());
            }
        }
    }

    /**
     * 群发消息
     */
    public static void sendToAll(String message) throws IOException {
        for (Map.Entry<String, WebSocketSession> entry : SESSIONS_MAP.entrySet()) {
            WebSocketSession session = entry.getValue();
            TextMessage textMessage = new TextMessage(message);
            session.sendMessage(textMessage);
        }
    }
}