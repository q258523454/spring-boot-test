package com.demo.controller;

import com.demo.config.AdminDestroyBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private AdminDestroyBean adminDestroyBean;

    @GetMapping("/index")
    public String index() {
        return "index";
    }


    @GetMapping("/delete/group")
    public String delete() {
        adminDestroyBean.deleteConsumeGroup();
        return "ok";
    }

    @GetMapping("/shutdown")
    public String shutdown() {
        System.exit(0);
        return "Application is shutting down...";
    }
}
