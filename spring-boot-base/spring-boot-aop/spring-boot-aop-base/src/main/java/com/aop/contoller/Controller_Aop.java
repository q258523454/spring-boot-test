package com.aop.contoller;

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
public class Controller_Aop {

    @RequestMapping(value = "/aop", method = RequestMethod.GET)
    public String aop() {
        log.info("请求AOP开始");
        // TODO 业务操作
        log.info("请求AOP执行完毕");
        return "请求AOP";
    }

    @RequestMapping(value = "/aop/exception", method = RequestMethod.GET)
    public String exception() throws Exception {
        log.info("exception");
        throw new Exception("测试异常");
    }
}
