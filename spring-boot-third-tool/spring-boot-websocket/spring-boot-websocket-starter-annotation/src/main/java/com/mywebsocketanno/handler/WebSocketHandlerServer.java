package com.mywebsocketanno.handler;


import com.alibaba.fastjson.JSONObject;
import com.mywebsocketanno.entity.ObjMessage;
import com.mywebsocketanno.manager.SessionManager;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


/**
 * 注意:
 * WebSocket 跟普通的 Bean 不一样, 它不是单例（Singleton）模式
 * 每次请求都会新建一个独立的链接，成员变量之间互不影响。
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{uid}")
public class WebSocketHandlerServer {

    /**
     * 当前连接会话id(独立)
     * 注意: WebSocket 跟普通的 Bean 不一样, 它不是单例（Singleton）模式, 每次请求都会新建一个独立的链接，成员变量之间互不影响。
     */
    private String uid;

    /**
     * 当前session(独立)
     * 注意: WebSocket 跟普通的 Bean 不一样, 它不是单例（Singleton）模式, 每次请求都会新建一个独立的链接，成员变量之间互不影响。
     */
    private Session session;

    /**
     * 建立连接
     *
     * @param uid     连接id,比如用户id
     * @param session 客户端会话
     */
    @OnOpen
    public void onOpen(@PathParam("uid") String uid, Session session) {
        // session id,自动生成, 从0开始累加
        String id = session.getId();
        log.info("客户端与服务端-正在建立连接. 客户端 UID:{}, session id:{}", uid, id);
        // 保存UID 和 session 关系
        SessionManager.addSession(uid, session);
        // 会话数 +1
        SessionManager.incrementSessionCount();
        // WebSocket 非单例模式bean, 成员变量是独立的
        this.uid = uid;
        this.session = session;
        log.info("客户端与服务端-连接成功. 客户端 UID:{}, session id:{}, 在线会话数:{}", uid, id, SessionManager.getSessionCount());
    }

    /**
     * 消息传递
     * 客户端: 发送消息调用该接口
     * 服务端: toSession.getAsyncRemote().sendText(xxx) 对客户端发送
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("服务接受到客户端消息. 消息:{}", message);
        // A发送消息给B，服务端收到A的消息后，从A的消息体中拿到B的uid及携带的手机号。查找B是否在线，如果B在线，则使用B的session发消息给B自己
        ObjMessage objMessage = JSONObject.parseObject(message, ObjMessage.class);
        String toUid = objMessage.getUid();
        String msg = objMessage.getMessage();
        log.info("服务接受到客户端消息. 客户端 UID:{} 对 客户端 UID:{},发送消息:{}", uid, toUid, msg);
        // A(uid)给B(toUid)发送消息
        SessionManager.sendToOne(toUid, msg);
    }


    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam("uid") String uid) {
        // 解除 UID 和 session 关系
        SessionManager.removeSession(uid);
        // 会话数 -1
        SessionManager.decrementSessionCount();
        log.info("客户端与服务端-连接关闭 session_id = {} UID = {} 关闭成功, 在线会话数:{} ", session.getId(), uid, SessionManager.getSessionCount());
    }

    /**
     * 发生错误调用的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误，错误信息为：" + error);
    }
}