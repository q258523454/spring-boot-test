package com.aysnc.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangj
 * @Date: 2019-09-10
 * @Version 1.0
 */

@Component("asyncUtil")
public class AsyncUtil {
    private static final Logger logger = LoggerFactory.getLogger(AsyncUtil.class);

    public void test() throws Exception {
        // test()->testAsync() :  异步线程失效，不允许普通方法调用异步方法，否则失效
        testAsync();
    }

    @Async
    public void testAsync() throws InterruptedException {
        // test()->testAsync() :  异步线程失效，不允许普通方法调用异步方法，否则失效
        // testAsync()->test() :  成功
        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        logger.info("进入@Async");
        Thread.sleep(2000);//让线程休眠，根据输出结果判断主线程和从线程是同步还是异步
        logger.info("退出@Async name:{} SID:{}", name, id);
    }
}
