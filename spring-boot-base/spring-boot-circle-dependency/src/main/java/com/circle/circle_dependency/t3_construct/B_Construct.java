//package com.circle.circle_dependency.t3_construct;
//
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@Data
//@Slf4j
//public class B_Construct {
//    private String name = "B3-构造函数注入";
//
//    private A_Construct aConstruct;
//
//    public B_Construct(A_Construct aConstruct) {
//        this.aConstruct = aConstruct;
//    }
//
//    @PostConstruct
//    public void init() {
//        log.info("------------------------");
//        log.info(aConstruct.getName());
//        log.info("------------------------");
//    }
//}
