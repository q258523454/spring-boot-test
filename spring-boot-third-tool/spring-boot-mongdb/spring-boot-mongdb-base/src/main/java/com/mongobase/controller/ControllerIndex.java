package com.mongobase.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class ControllerIndex {

    @GetMapping("/index")
    public String test() {
        log.info("index");
        return "index";
    }

}
