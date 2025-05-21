package com.inter.controller;

import com.alibaba.fastjson.JSONException;
import com.inter.entity.excpetion.NException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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