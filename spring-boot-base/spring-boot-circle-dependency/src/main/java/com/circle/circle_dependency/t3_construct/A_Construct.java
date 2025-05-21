package com.circle.circle_dependency.t3_construct;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Getter
@Component
@Slf4j
public class A_Construct {

    private String name = "A3-构造函数注入";

    private B_Construct bConstruct;

    public A_Construct(B_Construct bConstruct) {
        this.bConstruct = bConstruct;
    }

    @PostConstruct
    public void init() {
        log.info("------------------------");
        log.info(bConstruct.getName());
        log.info("------------------------");
    }

}
