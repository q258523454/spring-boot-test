package com.example.controller;

import com.example.service.ObserveService;
import com.example.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Observable;



@RestController
@RequestMapping("/ok")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping
    public String test1() {
        System.out.println("ok1");
        return "ok1";

    }
}
