package com.circle.circle_dependency.t1_autowired;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Getter
@Slf4j
public class B_Autowired {
    private String name = "B-Autowired";

    @Autowired
    private A_Autowired aAutowired;

    @PostConstruct
    public void init() {
        log.info("------------------------");
        log.info(aAutowired.getName());
        log.info("------------------------");
    }
}
