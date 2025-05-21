package com.provider.controller;

import com.alibaba.fastjson.JSON;
import com.provider.util.SpringContextHolder;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ContollerIndex {

    private static final Logger logger = LoggerFactory.getLogger(ContollerIndex.class);


    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }

    @GetMapping("/printProviderBean")
    public String printProviderBean() throws HttpException {
        try {
            System.out.println(JSON.toJSONString(SpringContextHolder.getBean("dubboPrintService")));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return "printProviderBean";
    }
}
