package com.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import java.io.IOException;


/**
 * 特性	            AsyncListener	                       SseEmitter
 * 核心功能	        监听Servlet异步请求的生命周期事件	       实现Server-Sent Events（SSE）的服务器端推送
 * 适用场景	        通用异步处理流程控制	                   单向实时数据推送（如SSE）
 * 实现层级	        Servlet API底层接口	                   Spring框架封装的高层工具类
 * 是否依赖          Servlet异步	                           必须与Servlet的AsyncContext结合使用	内部依赖Servlet异步，但对外隐藏实现细节
 * 双向通信支持	    无（仅监听事件）	                       无（SSE本身是单向的）
 * 业务封装	        无，纯事件监听	                           提供SSE协议的完整封装，简化开发
 * 典型使用场景	    异步任务超时处理、资源清理	               实时数据推送（如监控、通知）
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
