package com.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;

/**
 * @Author: zhangj
 * @Date: 2019-11-28
 * @Version 1.0
 */

@Component
public class MyListener {
    private static final Logger logger = LoggerFactory.getLogger(MyListener.class);


    private final List<SseEmitter> emitterList = new CopyOnWriteArrayList<>();

    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public void addSseEmitter(String id, SseEmitter sseEmitter) {
        sseEmitterMap.put(id, sseEmitter);
        logger.info("add sseEmitter, current num:{}", sseEmitterMap.size());

        sseEmitter.onCompletion(() -> {
            // TODO
            //logger.info("id:{} completed", id);
        });

        sseEmitter.onTimeout(() -> {
            // TODO
            //logger.info("id:{} timeout", id);
            // sseEmitter.completeWithError(new TimeoutException(id + "超时"));
        });
    }


    @EventListener
    public void pushEventToAll(String a) {
        // 监听事件, 根据参数类型来判断
        // 这里的监听事件不会触发, 因为没有 applicationContext.publishEvent(String)
        logger.info("listener2");
    }

    @EventListener
    public void pushEventToAll(MyEvent myEvent) {
        logger.info("Begin, total size:{}", sseEmitterMap.size());

        Iterator<Map.Entry<String, SseEmitter>> iterator = sseEmitterMap.entrySet().iterator();
        // 必须用迭代器, 因为要删除元素
        while (iterator.hasNext()) {
            Map.Entry<String, SseEmitter> entity = iterator.next();
            String id = entity.getKey();
            String msg = myEvent.getMsg();
            myEvent.setId(id);
            try {
                pushEvent(myEvent);
            } catch (Exception ex) {
                logger.error("id:{},消息推送异常.关闭连接", id);
                SseEmitter sseEmitter = sseEmitterMap.get(id);
                sseEmitter.completeWithError(ex);
                // 注意不是 sseEmitterMap.remove()
                iterator.remove();
                logger.info("id:{} 已关闭, size:{}", id, sseEmitterMap.size());
            }
        }
    }

    private void pushEvent(MyEvent myEvent) throws IOException {
        logger.info("开始推送消息给,id:{}", myEvent.getId());
        String id = myEvent.getId();
        String msg = myEvent.getMsg();
        SseEmitter sseEmitter = sseEmitterMap.get(id);
        sseEmitter.send(msg);
    }
}
