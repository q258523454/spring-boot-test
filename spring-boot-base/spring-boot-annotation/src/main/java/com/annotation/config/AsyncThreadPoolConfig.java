package com.annotation.config;

import com.annotation.util.AsyncLogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
@Import(value = {AsyncLogUtil.class})
public class AsyncThreadPoolConfig {

    @Value("${async.corePoolSize:10}")
    private int corePoolSize;
    @Value("${async.maxPoolSize:20}")
    private int maxPoolSize;
    @Value("${async.queueCapacity:10}")
    private int queueCapacity;

    // 默认bean(taskExecuotr), 也可以自己写@Bean("abc"), 注解上使用 @Async("abc)
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("My Log Task-");
        executor.initialize();
        return executor;
    }
}
