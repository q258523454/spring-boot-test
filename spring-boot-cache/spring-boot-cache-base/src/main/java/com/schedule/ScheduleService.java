package com.schedule;


import com.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScheduleService {

    @Autowired
    private CacheService cacheService;

    /**
     * 周一至周五 每两秒执行一次任务
     * cron表达式语法:[秒] [分] [小时] [日] [月] [周] [年]
     */
    @Scheduled(cron = "*/10 * * * * MON-FRI")
    public void refresh() {
        cacheService.refresh();
    }
}
