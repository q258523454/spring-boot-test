package com.ssepro.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


@Slf4j
@Service
public class SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private static final AtomicInteger CLIENT_COUNT = new AtomicInteger(0);

    public static int getClientCount() {
        return CLIENT_COUNT.intValue();
    }

    public SseEmitter createEmitter(String clientId, long timeout) {
        log.info("SseService createEmitter,clientId={},timeout={}", clientId, timeout);
        SseEmitter emitter = new SseEmitter(timeout);
        emitters.put(clientId, emitter);
        emitter.onCompletion(() -> doComplete(clientId));
        // 注意:超时执行 onTimeout() 后,会自动执行 onCompletion()
        emitter.onTimeout(() -> doTimeout(clientId));
        // 注意:onError() 是 send() 的过程中异常才会执行
        emitter.onError(e -> doError(clientId, e));
        CLIENT_COUNT.incrementAndGet();
        return emitter;
    }

    @Async("sseExecutor")
    public void sendToClient(String clientId, Object data) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                log.info("【Async】 SseService sendToClient,clientId:{},data:{}, start.", clientId, data);
                // 消息id
                String id = UUID.randomUUID().toString();
                emitter.send(SseEmitter.event()
                        .id(id)
                        .data(data));
            } catch (Exception e) {
                log.error("send occur error");
                // closeClient(clientId);
            }
        } else {
            log.warn("【Async】 SseService sendToClient,clientId:{} is null", clientId);
        }
    }

    @Async("sseExecutor")
    public void sendAll(Object data) {
        log.info("【Async】 SseService sendAll.");
        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            String clientId = entry.getKey();
            sendToClient(clientId, data);
        }
    }

    /**
     * 处理完成调用, 会关闭连接
     * 1.emitter.complete() 会异步调用onCompletion();
     * 2.emitter.completeWithError() 会异步调用onCompletion();
     */
    private void doComplete(String clientId) {
        log.info("SseService doComplete, clientId={}", clientId);
        emitters.remove(clientId);
        CLIENT_COUNT.decrementAndGet();
        log.info("SseService doComplete. client count:{}", CLIENT_COUNT.intValue());
    }

    /**
     * 超时处理逻辑
     * onTimeout() 执行完后,会自动执行 onCompletion()
     */
    private void doTimeout(String clientId) {
        log.info("doTimeout(), clientId:{}", clientId);
    }

    /**
     * 处理send()过程中异常逻辑
     * onError() 本地没有模拟成功,原因spring-webmvc等版本太低,升级到新版本即可
     * 例如
     * ① 客户端断开连接: emitter.send() 的时候, emitter 不存在, 已经断开连接了
     * ② 发送无效数据或异常数据: emitter.send() 的时候 发送的数据有问题
     *
     * doError() 执行完后,并没有继续执行 onCompletion()
     */
    private void doError(String clientId, Throwable e) {
        log.error("doError(), clientId:{} start.", clientId);
        emitters.remove(clientId);
        CLIENT_COUNT.decrementAndGet();
        log.info("doError(),clientId:{}. client count:{} end.", clientId,CLIENT_COUNT.intValue());
    }

    public void closeClient(String clientId) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            log.info("closeClient, clientId: {}", clientId);
            // 会异步调用 onCompletion()
            emitter.complete();
        } else {
            log.warn("closeClient,clientId:{} is null", clientId);
        }
    }

    public void closeAll() {
        log.info("SseService closeAll.");
        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            String clientId = entry.getKey();
            closeClient((clientId));
        }
    }

    public Map<String, SseEmitter> getEmitters() {
        return emitters;
    }
}