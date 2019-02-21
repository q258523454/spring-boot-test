package com.test.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by
 *
 * @author :   zhangjian
 * @date :   2018-08-27
 */

@RestController
public class Controller_Aop {

    private Logger log = LoggerFactory.getLogger(Controller_Aop.class);

    @RequestMapping(value = "/aop", method = RequestMethod.GET)
    public String aop() {
        log.info("aop");
        return "aop";
    }

    @RequestMapping(value = "/aopBefore", method = RequestMethod.GET)
    public String aopTest() {
        log.info("aopBefore");
        return "aopBefore";
    }

    @RequestMapping(value = "/aopAfter", method = RequestMethod.GET)
    public String aopAfter() {
        log.info("aopAfter");
        return "aopAfter";
    }

    @RequestMapping(value = "/doInsertStudentAfterAop", method = RequestMethod.GET)
    public String doInsertStudentAfterAop() {
        log.info("Controller_Aop doInsertStudentAfterAop() is done");
        return "doInsertStudentAfterAop";
    }

    @RequestMapping(value = "/aopHttpLog", method = RequestMethod.GET)
    public String aopHttpLog(@RequestParam("id") String httpId) {
        log.info("httpLog");
        return "httpLog";
    }

}
