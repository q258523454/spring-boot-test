package com.config;

import com.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @Description
 * @date 2020-07-31 14:17
 * @modify
 */


@Slf4j
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Autowired
    private DynamicConfig dynamicConfig;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        // 自定义线程池 (默认是单个执行器,多个定时任务之间是串行执行的)
        // 同样还可以 @Scheduled 方法上使用 @Async
        scheduledTaskRegistrar.setScheduler(taskScheduler());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Class<?> clazz;
                Object bean = (Object) SpringContextHolder.getBean(dynamicConfig.getBeanName());
                Method method = ReflectionUtils.findMethod(bean.getClass(), dynamicConfig.getMethod());
                log.info("任务{}已启动", dynamicConfig.getBeanName());
                ReflectionUtils.invokeMethod(method, bean);
                log.info("任务{}已结束", dynamicConfig.getBeanName());
            }
        };
        scheduledTaskRegistrar.addCronTask(runnable, dynamicConfig.getCron());
        log.info("任务{}已加载", dynamicConfig.getBeanName());
    }


    /**
     * 自定义线程池
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("dispatch-");
        scheduler.setAwaitTerminationSeconds(600);
        scheduler.setErrorHandler(throwable -> log.error("调度任务发生异常", throwable));
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

}
