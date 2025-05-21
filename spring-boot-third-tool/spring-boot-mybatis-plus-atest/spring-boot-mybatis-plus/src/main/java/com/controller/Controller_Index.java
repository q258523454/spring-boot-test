package com.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller_Index {

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/index2")
    public String indexPost() throws InterruptedException {
        return "post";
    }

    @PostMapping(value = "/jmeter")
    public String indexJmeter() throws InterruptedException {
        log.info("enter jmeter");
        Thread.sleep(5000);
        log.info("out");
        return "jmeter";
    }
}
