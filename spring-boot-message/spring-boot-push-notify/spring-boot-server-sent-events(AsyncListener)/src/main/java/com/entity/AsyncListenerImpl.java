package com.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

/**
 * @Author: zhangj
 * @Date: 2019-11-27
 * @Version 1.0
 */
public class AsyncListenerImpl implements AsyncListener {
    private static final Logger logger = LoggerFactory.getLogger(AsyncListenerImpl.class);

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        logger.info("推送结束");
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        logger.info("推送超时");
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        logger.info("推送错误");
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        logger.info("推送开始");
    }
}
