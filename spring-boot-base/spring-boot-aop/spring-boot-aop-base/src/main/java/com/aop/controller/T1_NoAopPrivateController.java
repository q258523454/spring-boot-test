package com.aop.controller;

import com.aop.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class T1_NoAopPrivateController {

    @Autowired
    private TestService testService;

    // 普通private可以正常被方法, InvocableHandlerMethod中调用的bean是实际对象本身
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    private void index() {
        testService.print();
        log.info("private index");
    }

}
