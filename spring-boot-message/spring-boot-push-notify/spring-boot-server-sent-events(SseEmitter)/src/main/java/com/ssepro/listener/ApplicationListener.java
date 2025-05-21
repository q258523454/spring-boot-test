package com.ssepro.listener;

import com.ssepro.service.SseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ApplicationListener {

    @Autowired
    private SseService sseService;

    /**
     * 监听事件, 根据参数类型来判断
     * com.listener.MyListener#pushEventToAll(java.lang.String)
     */
    @EventListener
    public void pushEventToAll(String str) {
        log.info("Application EventListener");
        sseService.sendAll(str);
    }

    /**
     * 异步发送
     * 监听事件, 根据参数类型来判断
     * com.listener.MyListener#pushEventToAll(double)
     */
    @EventListener
    @Async
    public void pushEventToAll(double d) {
        log.info("Application EventListener");
        sseService.sendAll(d);
    }
}
