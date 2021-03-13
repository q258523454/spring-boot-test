package com.life.controller;

import com.life.service.TestService;
import com.life.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhangj
 * @Date: 2019-09-10
 * @Version 1.0
 */


@Slf4j
@RestController
public class ControllerIndex {

    @GetMapping("/test")
    public String test() {
        TestService testService = SpringContextHolder.getBean("testSerivice");
        testService.hi();
        log.info("ok");
        return "ok";
    }

}
