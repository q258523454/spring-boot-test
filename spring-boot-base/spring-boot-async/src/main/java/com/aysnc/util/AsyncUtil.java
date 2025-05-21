package com.aysnc.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;



@Component("asyncUtil")
public class AsyncUtil {
    private static final Logger logger = LoggerFactory.getLogger(AsyncUtil.class);

    public void test() throws Exception {
        // test()->testAsync() :  异步线程失效，不允许普通方法调用异步方法，否则失效
        testAsync();
    }

    @Async("taskExecutor")
    public void testAsync() {
        // test()->testAsync() :  异步线程失效，不允许普通方法调用异步方法，否则失效
        // testAsync()->test() :  成功
        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        logger.info("-------------- 异步执行记录日志 start --------------");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("-------------- 异步执行记录日志 end --------------");
        logger.info("退出@Async name:{} SID:{}", name, id);
    }
}
