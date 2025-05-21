package com.mywebsocket.handler;

import com.mywebsocket.manager.SessionManager;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Configuration
public class MyWebSocketHandler implements WebSocketHandler {
    /**
     * 连接建立时调用
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("handler: connection established");
        String userId = (String) session.getAttributes().get("userId");
        // session id,自动生成, 从0开始累加
        String id = session.getId();
        WebSocketSession sessionInMap = SessionManager.getSession(userId);
        // session 本身已存在, 继续使用
        if (sessionInMap != null) {
            if (userId.equals(sessionInMap.getId())) {
                log.info("sessionId:{}, continue. ", sessionInMap.getId());
                session.sendMessage(new TextMessage("handler: connection continue"));
                return;
            }
        }
        // 保存 userId 和 session 关系
        SessionManager.add(userId, session);
        // 会话数 +1
        SessionManager.incrementSessionCount();

        session.sendMessage(new TextMessage("handler: connection established"));
        log.info("handler: userId:{}, session id:{}, count:{}", userId, id, SessionManager.getSessionCount());
    }

    /**
     * 处理接收到的消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("handler: handleMessage");
        String userId = (String) session.getAttributes().get("userId");

        if (message instanceof TextMessage) {
            String payload = ((TextMessage) message).getPayload();
            // 消息格式：userId:message
            String[] parts = payload.split(":", 2);
            if (parts.length == 2) {
                String toUserId = parts[0];
                String context = parts[1];
                SessionManager.sendToUser(parts[0], new TextMessage(context));
                log.info("handler: userId:{} to {}, message:{}", userId, toUserId, context);
            }
        } else {
            log.warn("handler: received message is null.");
        }
    }

    /**
     * 连接关闭 时调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("handler: connection closed");
        String userId = (String) session.getAttributes().get("userId");
        // 删除 session
        SessionManager.remove(userId);
        // 会话数 -1
        SessionManager.decrementSessionCount();
        log.info("handler: connection closed success, userId:{}, session id:{}, count:{} ", userId, session.getId(), SessionManager.getSessionCount());
    }

    /**
     * 连接错误 时调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.info("handler: handle transport error:", exception);
    }

    /**
     * 消息是否支持部分处理，默认是 false
     * 如果业务逻辑能够处理部分消息，那么可以返回 true 以提高处理效率
     */
    @Override
    public boolean supportsPartialMessages() {
        log.info("handler: supports partial messages");
        return false;
    }
}