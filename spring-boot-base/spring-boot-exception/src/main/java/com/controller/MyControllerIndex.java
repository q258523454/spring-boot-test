package com.inter.controller;

import com.inter.entity.excpetion.NException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyControllerIndex {

    public MyControllerIndex() {
        // Do nothing
    }

    @GetMapping("/")
    public String index() throws NException {
        return "index";
    }
}