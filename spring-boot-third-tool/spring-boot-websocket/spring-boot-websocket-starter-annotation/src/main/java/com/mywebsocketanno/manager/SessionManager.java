package com.mywebsocketanno.manager;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.Session;


@Slf4j
public enum SessionManager {
    ;
    /**
     * uid 绑定 session
     */
    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<String, Session>();

    /**
     * 会话数
     */
    private static final AtomicInteger SESSION_COUNT = new AtomicInteger(0);


    public static Map<String, Session> getSessionMap() {
        return SESSION_MAP;
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

    public static void removeSession(String uid) {
        SESSION_MAP.remove(uid);
    }

    public static void addSession(String uid, Session session) {
        SESSION_MAP.put(uid, session);
    }

    /**
     * 指定发送消息
     */
    public static void sendToOne(String toUid, String message) {
        Session toSession = SESSION_MAP.get(toUid);
        if (toSession == null) {
            log.error("会话ID:{} 不存在,无法发送 message:{}", toUid, message);
            return;
        }
        // 异步发送
        toSession.getAsyncRemote().sendText(message);
        log.info("服务端给 客户端 UID:{} 发送消息 message:{}, 成功.", toUid, message);
    }

    /**
     * 群发消息
     */
    public static void sendToAll(String message) {
        for (Map.Entry<String, Session> entry : getSessionMap().entrySet()) {
            Session session = entry.getValue();
            session.getAsyncRemote().sendText(message);
        }
    }
}