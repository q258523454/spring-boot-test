package com.rtuse.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @GetMapping(value = "/index")
    public String index() {
        log.info("index");
        return "index";
    }
}
