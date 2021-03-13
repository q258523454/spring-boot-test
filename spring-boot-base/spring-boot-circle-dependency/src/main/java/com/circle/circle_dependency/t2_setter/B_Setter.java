package com.circle.circle_dependency.t2_setter;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
@Slf4j
public class B_Setter {
    private String name = "B2-Setter注入";

    private A_Setter a2Setter;


    /**
     * setter注入: 由于setter方法是在对象实例化之后才完成的,因此可以解决循环依赖问题
     */
    @Autowired
    public void setAa(A_Setter a2Setter) {
        this.a2Setter = a2Setter;
    }

    @PostConstruct
    public void init() {
        log.info("------------------------");
        log.info(a2Setter.getName());
        log.info("------------------------");
    }
}
