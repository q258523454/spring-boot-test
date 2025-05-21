package com.aysnc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncThreadPoolConfig {
    private static final Logger logger = LoggerFactory.getLogger(AsyncThreadPoolConfig.class);
    @Value("${async.corePoolSize:10}")
    private int corePoolSize;
    @Value("${async.maxPoolSize:20}")
    private int maxPoolSize;
    @Value("${async.queueCapacity:10}")
    private int queueCapacity;

    // 默认bean(taskExecutor), 也可以自己写@Bean("abc"), 注解上使用 @Async("abc)
    @Bean
    public TaskExecutor taskExecutor() {
        /*
         * 默认使用 SimpleAsyncTaskExecutor
         * 该线程池每次执行都会创建线程，高并发或者访问很频繁的场景，很容易造成系统线程资源耗尽
         *
         */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("My Log Task-");
        executor.initialize();
        logger.info("TaskExecutor done");
        return executor;
    }
}
