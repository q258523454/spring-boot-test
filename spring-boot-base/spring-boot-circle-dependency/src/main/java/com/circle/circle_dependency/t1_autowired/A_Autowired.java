package com.circle.circle_dependency.t1_autowired;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Slf4j
@Getter
public class A_Autowired {

    private String name = "A-Autowired";

    @Autowired
    private B_Autowired bAutowired;

    @PostConstruct
    public void init() {
        log.info("------------------------");
        log.info(bAutowired.getName());
        log.info("------------------------");
    }

}
