package com.test.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created By
 *
 * @date :   2018-08-31
 */

/**
 * ServletContextListener
 * 主要用于资源加载, servlet启动最先加载资源，servlet关闭最后释放
 */
@WebListener
public class MyContextListener implements ServletContextListener {

    private final Logger log = LoggerFactory.getLogger(MyContextListener.class);


    /**
     * 主要用于启动资源加载, 可单独新开一个线程进行操作
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("============ Servlet Context 监听器: contextInitialized() ============");

    }

    /**
     * 容器销毁, 关闭资源
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("============ Servlet Context 监听器: contextDestroyed() ============");

    }
}
