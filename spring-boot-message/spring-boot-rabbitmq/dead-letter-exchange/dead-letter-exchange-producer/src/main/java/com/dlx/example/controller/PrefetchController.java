package com.dlx.example.controller;

import com.dlx.example.sender.PrefetchSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class PrefetchController {

    AtomicInteger atomicInteger = new AtomicInteger(0);


    @Autowired
    private PrefetchSender prefetchSender;

    @GetMapping(value = "/send/prefetch/1")
    public String send1() {
        prefetchSender.send("ok");
        return "success";
    }

    @GetMapping(value = "/send/prefetch/10")
    public String send10() {
        for (int j = 0; j < 10; j++) {
            int i = atomicInteger.addAndGet(1);
            prefetchSender.send("ok-" + i);
        }
        return "success";
    }

}
