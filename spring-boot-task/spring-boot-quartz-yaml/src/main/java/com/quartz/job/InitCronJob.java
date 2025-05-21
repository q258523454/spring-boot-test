package com.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Date: 2019-05-29
 * @Version 1.0
 */

// DisallowConcurrentExecution 【强制】 定时任务必须等待前一次定时任务完成
// 如果不加 @DisallowConcurrentExecution , Quartz默认为多线程并发执行， 不会等待前一次任务执行完毕
@DisallowConcurrentExecution
public class InitCronJob implements Job {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    // JobExecutionContext中封装有Quartz运行所需要的所有信息[可自定义信息]
    // JobExecutionException execute()方法只允许抛出JobExecutionException异常
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        log.info("========================= 初始 定时任务===============================");
        log.info("time=====:" + simpleDateFormat.format(Calendar.getInstance().getTime()));
        log.info("jobName=====:" + jobExecutionContext.getJobDetail().getKey().getName());
        log.info("jobGroup=====:" + jobExecutionContext.getJobDetail().getKey().getGroup());
        log.info("initData=====:" + jobExecutionContext.getJobDetail().getJobDataMap().get("initData"));
        log.info("sleep start");
        for (int i = 0; i < 3; i++) {
            log.warn("jobName:[{}],jobGroup[{}]", jobExecutionContext.getJobDetail().getKey().getName(), jobExecutionContext.getJobDetail().getKey().getGroup());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("sleep done");
    }
}
