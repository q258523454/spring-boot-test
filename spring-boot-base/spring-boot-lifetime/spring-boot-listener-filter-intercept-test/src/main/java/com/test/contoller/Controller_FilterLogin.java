package com.test.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by
 *
 * @date :   2018-08-27
 */

@RestController
public class Controller_FilterLogin {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() throws Exception {
        log.info("login success.");
        return "login success";
    }
}
