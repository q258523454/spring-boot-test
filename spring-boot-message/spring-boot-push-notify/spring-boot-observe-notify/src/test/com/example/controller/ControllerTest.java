package com.example.controller;

import com.example.ObserveNotifyApplication;
import com.example.service.ObserveService;
import com.example.util.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Observable;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ObserveNotifyApplication.class)
public class ControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ControllerTest.class);
    @Autowired
    public Observable observable;

    @Autowired
    public ObserveService observeService;

    @Autowired
    private SpringContextHolder springContextHolder;

    @Test
    public void test1() {
        String res = observeService.doBussiness("do a bussiness");
        logger.info(res);
        logger.info("ok");
    }
}