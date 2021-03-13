package com.example.controller;

import com.example.producer.DirectSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectController {
    @Autowired
    private DirectSender directSender;

    @GetMapping("/sendDirectQueue")
    public Object sendDirectQueue() {
        directSender.sendDirectQueue();
        return "ok";
    }
}