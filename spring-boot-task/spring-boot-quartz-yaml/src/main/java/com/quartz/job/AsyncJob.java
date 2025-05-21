package com.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Date: 2019-05-29
 * @Version 1.0
 */
public class AsyncJob implements Job {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * JobExecutionContext中封装有Quartz运行所需要的所有信息[可自定义信息]
     * JobExecutionException execute()方法只允许抛出JobExecutionException异常
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("========================立即执行的任务，只执行一次===============================");
        log.info("jobName=====:" + jobExecutionContext.getJobDetail().getKey().getName());
        log.info("jobGroup=====:" + jobExecutionContext.getJobDetail().getKey().getGroup());
        log.info("taskData=====:" + jobExecutionContext.getJobDetail().getJobDataMap().get("asyncData"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("taskData=====:done");
    }
}
