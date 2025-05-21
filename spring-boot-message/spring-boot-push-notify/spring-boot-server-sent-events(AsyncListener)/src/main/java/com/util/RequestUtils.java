package com.util;

import com.entity.AsyncListenerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    /**
     * 超时时间
     */
    private static final int TIME_OUT = 60 * 60 * 1000;

    /**
     * 订阅列表，存储所有主题的订阅请求，每个topic对应一个ArrayList，ArrayList里该topic的所有订阅请求
     */
    private static HashMap<String, ArrayList<AsyncContext>> subscribeArray = new LinkedHashMap<>();

    /**
     * 添加订阅消息
     */
    public static void addSubscrib(String topic, HttpServletRequest request, HttpServletResponse response) {
        if (null == topic || "".equals(topic)) {
            return;
        }
        // 设置响应头ContentType
        response.setContentType("text/event-stream");
        // 设置响应编码类型
        response.setCharacterEncoding("UTF-8");
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        // 支持异步响应,异步这个概念很多地方都有，就像处理文件时，不是一直等待文件读完，而是让它去读，cpu做其它事情，读完通知cpu来处理即可。
        AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(TIME_OUT);
        asyncContext.addListener(new AsyncListenerImpl());
        ArrayList<AsyncContext> asyncContextList = subscribeArray.get(topic);
        if (null == asyncContextList) {
            asyncContextList = new ArrayList<AsyncContext>();
            subscribeArray.put(topic, asyncContextList);
        }
        asyncContextList.add(asyncContext);
    }

    /**
     * 获取订阅列表
     */
    public static ArrayList<AsyncContext> getSubscribList(String topic) {
        return subscribeArray.get(topic);
    }

    /**
     * 推送消息
     */
    public static void publishMessage(String topic, String content) {
        ArrayList<AsyncContext> asyncContextList = subscribeArray.get(topic);
        logger.info("topic:{}, subscribe nums:{}", topic, asyncContextList.size());
        if (asyncContextList.isEmpty()) {
            return;
        }
        for (AsyncContext asyncContext : asyncContextList) {
            if (asyncContext.getTimeout() < 0) {
                asyncContextList.remove(asyncContext);
                logger.warn("有客户端断开连接,subscribe nums:{}", asyncContextList.size() - 1);
            }
            try {
                PrintWriter out = asyncContext.getResponse().getWriter();
                if (out.checkError()) {
                    logger.warn("PrintWriter error, 断开连接");
                    asyncContextList.remove(asyncContext);
                }
                logger.info("推送消息:{}", content);
                // \n\n是返回的格式写法
                out.print(content + "\n\n");
                asyncContext.getResponse().flushBuffer();
                out.flush();
            } catch (Exception e) {
                logger.warn("出现异常, 断开连接");
                asyncContextList.remove(asyncContext);
                e.printStackTrace();
            }
        }
    }
}
