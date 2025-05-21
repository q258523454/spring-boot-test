package com.rtuse.controller;

import com.ratelimitbase.utils.AopUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimiterKeyTest {
    public static void main(String[] args) {
        /*
         *  需要动态修改生产环境的限流策略
         *  获取 全限定名称(带参数)
         *  例如:
         *  返回值:com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String)
         *  生产在 application.properties 添加
         *  com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).limit=5 --- 【必须】
         *  com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).type=guava --- 【必须】
         *  com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).spel=#student.age,#student.id --- 【非必须】
         *  com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).callBackMethod=callBack2 --- 【非必须】
         *  上述值将替换对应注解的值,实现动态修改
         */
        System.out.println((AopUtils.getFullyMethodPathWithParameter(TestGuavaController.class, "test1")));
    }
}
