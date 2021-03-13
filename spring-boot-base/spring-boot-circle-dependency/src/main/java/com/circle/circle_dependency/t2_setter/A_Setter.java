package com.circle.circle_dependency.t2_setter;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Getter
@Component
@Slf4j
public class A_Setter {

    private String name = "A2-Setter注入";

    private B_Setter bSetter;

    /**
     * setter注入: 由于setter方法是在对象实例化之后才完成的,因此可以解决循环依赖问题
     */
    @Autowired
    public void setBSetter(B_Setter bSetter) {
        this.bSetter = bSetter;
    }


    @PostConstruct
    public void init() {
        log.info("------------------------");
        log.info(bSetter.getName());
        log.info("------------------------");
    }

}
