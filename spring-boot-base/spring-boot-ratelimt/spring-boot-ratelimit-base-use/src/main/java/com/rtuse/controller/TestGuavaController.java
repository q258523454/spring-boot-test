package com.rtuse.controller;


import com.alibaba.fastjson.JSON;
import com.ratelimitbase.aspect.RateLimiterAnnotation;
import com.ratelimitbase.enums.RateLimiterTypeEnum;
import com.rtuse.pojo.Student;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestGuavaController {

    /**
     * 在api或者方法上加注解,指定type和limit即可
     */
    @PostMapping(value = "/ratelimit/guava/dafault")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.GUAVA, limit = 10)
    public String test1(@RequestBody Student student, @RequestParam String id) {
        log.info("test1");
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "index";
    }

    /**
     * callBackMethod 指定降级策略
     * callBackMethod 指定命中限流策略后, 回调的方法（降级策略）, 不指定则会直接打印异常
     */
    @PostMapping(value = "/ratelimit/guava/callback")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.GUAVA, limit = 5, callBackMethod = "callBack1")
    public String test3(@RequestBody Student student, @RequestParam String id) {
        log.info("test3");
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "index";
    }

    /**
     * 限流后降级处理方法,通过反射获取
     * 1.必须与限流方法在同个类下定义;
     * 2.入参必须与限流接口保持一致;
     */
    public void callBack1(Student student, String id) {
        log.info("callBack1");
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        log.error("callBack1 execute");
    }


    /**
     * SpEL表达式, 动态指定限流key后缀
     * 注意:表达式不要为空, 同时要考虑SpEL的值不能为空, 为空则会忽略SpEL, 因此尽量保证SpEL一定有值.
     * 例如:这里根据指定 id 限流
     */
    @PostMapping(value = "/ratelimit/guava/spel")
    @RateLimiterAnnotation(spel = {"#student.id", "#student.age"}, type = RateLimiterTypeEnum.GUAVA, limit = 5)
    public String test4(@RequestBody Student student, @RequestParam String id) {
        log.info("test4");
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "spel";
    }


    /**
     * 动态修改限流策略
     * 通过 propeties 修改限流策略(配置读取,优先级最高)
     * 例如在application.properties新增配置:
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).limit=5
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).type=guava
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).spel=#student.age,#student.id
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).callBackMethod=callBack2
     * 下面的注解值将全部被替换,实现动态修改
     */
    @PostMapping(value = "/ratelimit/guava/readproperties")
    @RateLimiterAnnotation(spel = {"#student.id"}, type = RateLimiterTypeEnum.GUAVA, limit = 100, callBackMethod = "callBack1")
    public String readProperties(@RequestBody Student student, @RequestParam String id) {
        log.info("readProperties");
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "index";
    }

    public void callBack2(Student student, String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        log.error("callBack2 execute");
    }
}
