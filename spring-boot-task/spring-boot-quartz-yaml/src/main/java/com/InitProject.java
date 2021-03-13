package com;

import com.quartzjob.CronJob;
import com.quartzjob.InitCronJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @Date: 2019-05-29
 * @Version 1.0
 */

@Service
public class InitProject {

    private static Logger log = LoggerFactory.getLogger(InitProject.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    // 下面的初始化jobname和group不要与其他jobname和group重复, 也不要更改, 集群部署s 根据这两个名字重置的
    public static String initCronJobName = "init_job";      // 启动任务 name

    public static String initCronJobGroup = "init_group";   // 启动任务 group


    // 在Spring @value执行(即所有依赖注入完成)之后执行
    @PostConstruct
    public void initMethod() {
        try {
            // 0/5 * * * * ?; 每5秒钟运行一次
            // 0 0/5 * * * ?; 每5分钟运行一次
            String crontab = "*/5 * * * * ?";
            initCronJob(crontab);
        } catch (Exception e) {
            log.error("printStackTrace ", e);
        }
    }

    /**
     * 初始化或重置(针对集群部署)定时任务 Job("init_job","init_group")
     */
    private void initCronJob(String crontab) throws SchedulerException {
        synchronized (log) {                                                            // 只允许一个线程进入操作
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());

            for (JobKey jobKey : set) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);                   // 获取jooDetail, 获取各种job信息, 用来重建
                // [重要] 如果存在启动定时任务, 先删除
                if (null != jobDetail && InitProject.initCronJobName.equals(jobDetail.getKey().getName()) && InitProject.initCronJobGroup.equals(jobDetail.getKey().getGroup())) {
                    JobKey initJobKey = jobDetail.getKey();
                    log.warn("Programming detects the Job[name:{},group:{}] task already exist , ready to reset the init quartz job task.", initJobKey.getName(), initJobKey.getGroup());
                    // 先依次暂停、卸载、删除
                    scheduler.pauseJob(jobKey); //暂停该Job
                    scheduler.unscheduleJob(TriggerKey.triggerKey(initJobKey.getName(), initJobKey.getGroup()));    // 卸载触发器(删除数据库的前一步操作)
                    scheduler.deleteJob(jobKey);                                                                   // 删除任务(从数据库删除)
                }
            }

            log.warn("Before task have finished delete, now start to reset the Job[name:{},group:{}].", InitProject.initCronJobName, InitProject.initCronJobGroup);
            // ---------------------------- 将启动定时任务加入Quartz BEGIN ----------------------------
            // 构建job信息
            JobDetail newJobDetail = JobBuilder.newJob(InitCronJob.class)
                    .withIdentity(InitProject.initCronJobName, InitProject.initCronJobGroup)
                    // storeDurably() 在没有触发器指向任务的时候，使用 sched.addJob(quartzjob, true) 将任务保存在队列中了。而后使用 sched.scheduleJob 触发。
                    // 如果不使用 storeDurably ，则在添加 Job 到引擎的时候会抛异常，意思就是该 Job 没有对应的 Trigger。
                    .storeDurably()
                    .build();

            // 用JopDataMap在同一个任务之间传递数据
            newJobDetail.getJobDataMap().put("cron_expression", crontab);

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(crontab);

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(newJobDetail.getKey().getName() + "_trigger", newJobDetail.getKey().getGroup() + "_trigger")
                    .withSchedule(scheduleBuilder)
                    .build();

            // 创建任务,并将触发器和任务列表绑定
            scheduler.scheduleJob(newJobDetail, trigger);
            // ---------------------------- 将启动定时任务加入Quartz END ----------------------------
        }
    }
}
