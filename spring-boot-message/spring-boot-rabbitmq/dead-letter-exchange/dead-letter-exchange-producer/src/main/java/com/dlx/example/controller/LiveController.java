package com.dlx.example.controller;

import com.dlx.example.sender.LiveSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class LiveController {

    AtomicInteger atomicInteger = new AtomicInteger(0);


    @Autowired
    private LiveSender liveSender;

    @GetMapping(value = "/send/live")
    public String send() {
        liveSender.send("ok");
        return "success";
    }


}
