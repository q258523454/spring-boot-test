package com.quartz.controller;

import com.quartz.service.JobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-05-29
 * @Version 1.0
 */
@RestController
public class Controller_Quartz {


    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private JobService jobService;

    /**
     * 创建cron任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @PostMapping(value = "/cron")
    public String startCronJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup,
            @RequestParam("crontab") String crontab) {
        jobService.addCronJob(jobName, jobGroup, crontab);
        return "create cron task success";
    }

    /**
     * 创建异步任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/async", method = RequestMethod.POST)
    public String startAsyncJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.addAsyncJob(jobName, jobGroup);
        return "create async task success";
    }

    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    public String pauseJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.pauseJob(jobName, jobGroup);
        return "pause quartzjob success";
    }

    /**
     * 恢复任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    public String resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.resumeJob(jobName, jobGroup);
        return "resume quartzjob success";
    }

    /**
     * 删除务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public String deleteJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        jobService.deleteJob(jobName, jobGroup);
        return "delete quartzjob success";
    }

}
