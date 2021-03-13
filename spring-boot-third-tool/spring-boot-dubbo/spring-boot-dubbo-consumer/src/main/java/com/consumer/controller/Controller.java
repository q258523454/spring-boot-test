package com.consumer.controller;

import com.consumer.service.impl.DubboPrintConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-08-29
 * @Version 1.0
 */

@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private DubboPrintConsumer dubboPrintConsumer;

    @GetMapping(value = "/dubboPrint", produces = "application/json; charset=UTF-8")
    public String dubboPrint() {
        String fromProvider= dubboPrintConsumer.getPrintService1().print("consumer1,consumer3");
        logger.info("Call Provider Success: {}", fromProvider);
        return "success";
    }
}
