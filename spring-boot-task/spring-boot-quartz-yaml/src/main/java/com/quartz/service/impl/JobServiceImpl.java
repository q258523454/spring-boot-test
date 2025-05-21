package com.quartz.service.impl;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import com.alibaba.fastjson.JSON;
import com.quartz.job.AsyncJob;
import com.quartz.job.CronJob;
import com.quartz.service.JobService;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2019-05-29
 * @Version 1.0
 */

@Service
public class JobServiceImpl implements JobService {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    /**
     * 创建一个定时任务
     */
    @Override
    public void addCronJob(String jobName, String jobGroup, String crontab) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail != null) {
                List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
                System.out.println("quartzjob:" + jobName + " 已存在:" + JSON.toJSONString(triggersOfJob));
            } else {
                // 构建job信息
                jobDetail = JobBuilder.newJob(CronJob.class)
                        .withIdentity(jobName, jobGroup)
                        // 是否job持久化到数据库中
                        // false: 当删除Trigger的时候都会级联删除JobDetails;
                        // true: Trigger 删除的时候 JobDetails(quartz_job_details) 信息存在
                        .storeDurably(true) // 即使该JobDetail没有关联的Trigger，也会进行存储
                        .build();

                // 用JopDataMap来传递数据
                jobDetail.getJobDataMap().put("taskData", "hzb-cron-001");

                // 表达式调度构建器(即任务执行的时间,每5秒执行一次)
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(crontab);

                // 按新的cronExpression表达式构建一个新的trigger
                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName + "_trigger", jobGroup + "_trigger")
                        .withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加异步任务
     *
     * @param jobName
     * @param jobGroup
     */
    @Override
    public void addAsyncJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail != null) {
                System.out.println("quartzjob:" + jobName + " 已存在");
            } else {
                // 构建job信息,在用JobBuilder创建JobDetail的时候，有一个storeDurably()方法，可以在没有触发器指向任务的时候，将任务保存在队列中了。然后就能手动触发了
                jobDetail = JobBuilder.newJob(AsyncJob.class).withIdentity(jobName, jobGroup).storeDurably().build();
                jobDetail.getJobDataMap().put("asyncData", "this is a async task");
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName + "_trigger", jobGroup + "_trigger") // 定义name/group
                        .startNow()// 一旦加入scheduler，立即生效, 执行一次的定时任务（异步任务）
                        .withSchedule(simpleSchedule())// 使用SimpleTrigger
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void pauseJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
            // 1个任务(JobDetail)可以绑定多个Trigger，但一个Trigger只能绑定一个任务
            for (Trigger trigger : triggersOfJob) {
                TriggerKey triggerKey = trigger.getKey();
                scheduler.pauseTrigger(triggerKey);
            }
            System.out.println("=========================pause quartzjob:" + jobName + " success========================");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复任务
     */
    @Override
    public void resumeJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
            // 1个任务(JobDetail)可以绑定多个Trigger，但一个Trigger只能绑定一个任务
            for (Trigger trigger : triggersOfJob) {
                TriggerKey triggerKey = trigger.getKey();
                scheduler.resumeTrigger(triggerKey);
            }
            System.out.println("=========================resume quartzjob:" + jobName + " success========================");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除任务
     */
    @Override
    public void deleteJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            // 删除整个任务 JobDetail
            scheduler.deleteJob(jobKey);
            System.out.println("=========================delete quartzjob:" + jobName + " success========================");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
