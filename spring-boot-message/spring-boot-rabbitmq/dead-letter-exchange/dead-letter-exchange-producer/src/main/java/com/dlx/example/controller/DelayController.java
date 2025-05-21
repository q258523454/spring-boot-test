package com.dlx.example.controller;

import com.dlx.example.sender.DelaySender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DelayController {

    @Autowired
    private DelaySender delaySender;

    @GetMapping(value = "/send/delay")
    public String send() {
        delaySender.send();
        return "success";
    }

}
