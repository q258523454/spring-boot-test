package com.aop.controller;


import com.aop.service.JdkControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class T3_JdkProxyController implements JdkControllerService {

    /**
     * 默认Cglib代理,没有问题.
     *
     * 测试jdk代理
     * 1.proxy-target-class: false
     * 2.实现一个接口
     *
     * 结果：不管这个类有没有被AOP拦截, 访问都会报错404
     *
     * 原因：
     * 不管是JDK动态代理还是CGLIB动态代理，此时的bean都是代理对象。检测bean上的方法，一定得检测真实的目标对象才有意义。
     * Spring获取代理的实际对象方法是通过{@link org.springframework.util.ClassUtils}中 getUserClass() 方法获取的。
     * 其中有一个判断 if(clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) 表示只有Cglib代理才能获取真正实例对象(superClass).
     * 如果JDK代理无法获取实际代理对象,那么就无法获取对象方法的上的注解。
     */
    @RequestMapping(value = "/jdk/noaop", method = RequestMethod.GET)
    public void noaop() {
        print();
    }

    // 正常
    @RequestMapping(value = "/jdk/aop", method = RequestMethod.GET)
    public String aop() {
        print();
        return "aop";
    }


    @Override
    public void print() {
        log.info("print");
    }
}
