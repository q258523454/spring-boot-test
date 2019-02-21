package com.test.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-31
 */

// ServletRequestListener: 主要用于
@WebListener
public class MyRequestListener implements ServletRequestListener {

    private final Logger log = LoggerFactory.getLogger(MyRequestListener.class);

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        log.info("============ Servlet Request 监听器: requestDestroyed() ============");
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        log.info("============ Servlet Request 监听器: requestInitialized() ============");
    }
}
