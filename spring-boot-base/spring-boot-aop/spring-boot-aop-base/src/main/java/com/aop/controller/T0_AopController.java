package com.aop.controller;

import lombok.extern.slf4j.Slf4j;
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
    @RequestMapping(value = "/aop", method = RequestMethod.GET)
    public String aop() {
        log.info("请求AOP");
        return "请求AOP";
    }
}
