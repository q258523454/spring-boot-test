package com.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class ScheduleService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     *  周一至周五 每两秒执行一次任务
     *  cron表达式语法:[秒] [分] [小时] [日] [月] [周] [年]
     */
    @Scheduled(cron = "*/5 * * * * MON-FRI")
    public void testAsyncJob() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("现在时间1：" + dateFormat.format(new Date()));
    }

    @Scheduled(cron = "*/5 * * * * MON-FRI")
    public void testAsyncJob2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("现在时间2：" + dateFormat.format(new Date()));
    }
}
