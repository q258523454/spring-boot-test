package com.aop.controller;

import com.aop.service.TestService;
import com.aop.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@Slf4j
@DependsOn("springContextHolder")
public class T2_AopAutowiredController {

    @Autowired
    private TestService testService;

    @PostConstruct
    public void init() {
        // 证明: bean 已经注入, 但是 private 方法里面为null
        log.info("testService bean:" + testService);
        log.info("testService bean:" + SpringContextHolder.getBean(TestService.class));
    }

    /**
     * 总结:
     * 当被 @Autowired 注入的 bean 被切面AOP拦截后
     * public 修饰的方法里面 bean 正常使用
     * private 修饰的方法里面 bean=null, 可以通过 SpringContextHolder 获取使用
     * 实际上不管public还是private,InvocableHandlerMethod.doInvoke()中都是代理对象,bean都是null.
     * 正在原因是：代理类中 private 方法无法直接获取被代理对象本身
     */
    @GetMapping(value = "/publicPrint")
    public String publicPrint() {
        testService.print();
        return "ok";
    }

    /**
     * 访问修饰符 private
     * 无法被代理，但这个不是bean=null的原因。因为只是该方法无法被代理而已。
     * 原因：由于Controller类这个被代理了,beanInvocableHandlerMethod.doInvoke()
     */
    @GetMapping(value = "/privatePrint")
    private String privatePrint() {
        // 下面bean为null, 可以通过 SpringContextHolder主动去容器拿bean
        testService.print();
        return "ok";
    }
}
