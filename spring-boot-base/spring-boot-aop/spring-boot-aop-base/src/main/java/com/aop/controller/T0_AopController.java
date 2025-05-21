package com.aop.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by
 *
 * @date :   2018-08-27
 */

@RestController
@Slf4j
public class T0_AopController {

    /**
     * 返回参数必须 String 才能被AOP 拦截
     */
    @GetMapping(value = "/aop")
    public String aop1() {
        log.info("请求AOP");
        return "请求AOP";
    }

    @GetMapping(value = "/aop/error")
    public String aop2() {
        log.info("请求AOP");
        throw new RuntimeException("erro");
    }
}
