package com.consumer.controller;

import com.consumer.util.SpringContextHolder;
import com.share.service.IDubboPrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-08-29
 * @Version 1.0
 */

@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);


    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }

    @Autowired
    @Qualifier(value = "Consume.BasicService")
    private IDubboPrintService printService;

    @GetMapping(value = "/printConsumerBean", produces = "application/json; charset=UTF-8")
    public String printConsumerBean() {
        logger.info(printService.print("printConsumerBean"));
        try {
            IDubboPrintService dubboPrintService = SpringContextHolder.getBean("Consume.BasicService");
            System.out.println(dubboPrintService.print("printConsumerBean"));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return "success";
    }

}
