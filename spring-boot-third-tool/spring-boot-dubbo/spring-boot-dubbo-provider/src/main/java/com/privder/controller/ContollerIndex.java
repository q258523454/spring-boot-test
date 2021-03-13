package com.privder.controller;

import com.alibaba.fastjson.JSON;
import com.share.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhangj
 * @Date: 2019-09-23
 * @Version 1.0
 */

@RestController
public class ContollerIndex {

    private static final Logger logger = LoggerFactory.getLogger(ContollerIndex.class);


    @GetMapping("/printProviderBean")
    public String printProviderBean() {
        try {
            System.out.println(JSON.toJSONString(SpringContextHolder.getBean("DP.PrintService1")));
            System.out.println(JSON.toJSONString(SpringContextHolder.getBean("DP.PrintService2")));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return "testContextUtil";
    }

}
