package com.aysnc.controller;

import com.aysnc.util.AsyncUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private AsyncUtil asyncUtil;

    @GetMapping("/")
    public String index() throws Exception {
        return "index";
    }

    @GetMapping("/test")
    public void test() throws Exception {
        asyncUtil.test();
        logger.info("/test request has finished ");
    }

    @GetMapping("/testAsync")
    public void testAsync() throws Exception {
        asyncUtil.testAsync();
        logger.info("/testAsync request has finished ");

    }
}
