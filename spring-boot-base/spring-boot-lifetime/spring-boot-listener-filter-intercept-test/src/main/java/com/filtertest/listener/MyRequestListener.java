package com.filtertest.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class MyRequestListener implements ServletRequestListener {

    private final Logger log = LoggerFactory.getLogger(MyRequestListener.class);

    /**
     * 可对请求最早进入的时候进行定制化处理
     */
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        log.info("============ Servlet Request 监听器: requestInitialized() ============");
    }

    /**
     * 可对请求最后出去的时候进行定制化处理
     */
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        log.info("============ Servlet Request 监听器: requestDestroyed() ============");
    }
}
