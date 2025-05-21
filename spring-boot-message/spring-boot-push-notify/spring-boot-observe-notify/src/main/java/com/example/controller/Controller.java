package com.example.controller;

import com.example.service.ObserveService;
import com.example.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Observable;



@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    public Observable observable;

    @Autowired
    public ObserveService observeService;

    @Autowired
    private SpringContextHolder springContextHolder;

    @GetMapping("/test")
    public void test() {
        String res = observeService.doBussiness("do a bussiness");
        System.out.println(res);
        System.out.println("ok");
    }
}
