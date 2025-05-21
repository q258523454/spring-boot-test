package com.inter.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller_Index {

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }
}
