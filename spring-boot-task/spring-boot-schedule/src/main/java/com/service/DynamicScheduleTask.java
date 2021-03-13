package com.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@Service("dynamicScheduleTask")
public class DynamicScheduleTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void method() {
        log.info("method()：" + dateFormat.format(new Date()));
    }
}
