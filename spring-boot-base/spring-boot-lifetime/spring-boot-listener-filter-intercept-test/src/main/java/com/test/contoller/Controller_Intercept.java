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
public class Controller_Intercept {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/intercept", method = RequestMethod.GET)
    public String intercept() throws Exception{
        log.info("intercept success");
        return "intercept success";
    }

}
