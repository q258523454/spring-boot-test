package com.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Date: 2019-05-29
 * @Version 1.0
 */
public class CronJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("=========================定时任务每5秒执行一次===============================");
        System.out.println("jobName=====:"+jobExecutionContext.getJobDetail().getKey().getName());
        System.out.println("jobGroup=====:"+jobExecutionContext.getJobDetail().getKey().getGroup());
        System.out.println("taskData=====:"+jobExecutionContext.getJobDetail().getJobDataMap().get("taskData"));
    }
}
