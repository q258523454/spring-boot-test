package com.quartzjob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Date: 2019-05-29
 * @Version 1.0
 */
public class AsyncJob implements Job {


    // JobExecutionContext中封装有Quartz运行所需要的所有信息[可自定义信息]
    // JobExecutionException execute()方法只允许抛出JobExecutionException异常
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("========================立即执行的任务，只执行一次===============================");
        System.out.println("jobName=====:" + jobExecutionContext.getJobDetail().getKey().getName());
        System.out.println("jobGroup=====:" + jobExecutionContext.getJobDetail().getKey().getGroup());
        System.out.println("taskData=====:" + jobExecutionContext.getJobDetail().getJobDataMap().get("asyncData"));
    }
}
