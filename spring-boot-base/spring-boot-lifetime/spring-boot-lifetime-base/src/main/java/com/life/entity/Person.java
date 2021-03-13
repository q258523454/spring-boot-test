package com.life.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;


/**
 * Bean自身的方法和Bean级生命周期接口方法
 * 为了方便演示，它实现了BeanNameAware、BeanFactoryAware、InitializingBean和DiposableBean这4个接口
 */
@Slf4j
public class Person implements BeanNameAware, BeanFactoryAware, InitializingBean, DisposableBean {

    private String name;
    private int age;
    private BeanFactory beanFactory;
    private String beanName;

    public Person() {
        System.out.println("【Person构造函数】Person()name=" + name + ",age=" + age);
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("【Person构造函数】Person()name=" + name + ",age=" + age);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("Person setter注入 name= " + name);
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        System.out.println("Person setter注入 age=" + age);
        this.age = age;
    }


    /**
     * 通过<bean>的init-method属性指定的初始化方法
     * 或者通过@Bean(initMethod = "myInit")指定
     * 注: 实际上调用是 InitializingBean.afterPropertiesSet()
     */
    @Bean
    public void myInit() {
        System.out.println("【initMethod()】执行 initMethod 方法,init-method属性指定的方法");
    }


    @Override
    public void setBeanName(String s) {
        System.out.println("【BeanNameAware】执行 setBeanName 方法。设置Bean的ID为" + s);
        this.beanName = s;
    }

    // 让Bean拥有访问Spring容器的能力,spring的api耦合在一起，这种方式不推荐
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("【BeanFactoryAware】执行 @setBeanFactory 方法,让Bean拥有访问Spring容器的能力");
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void myPostConstruct() {
        System.out.println("【@PostConstruct】执行 PostConstruct 方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("【InitializingBean】执行 afterPropertiesSet 方法。备注:配置读取后的方法,可用修改实体属性");
        System.out.println("修改前的配置:name=" + name + ",age=" + age);
        this.setName("zhangxiaofan");
        this.setAge(88);
        System.out.println("修改后的配置:name=" + name + ",age=" + age);

    }

    @Override
    public void destroy() throws Exception {
        System.out.println("【DiposibleBean】执行 destroy 方法");
    }


    // 通过<bean>的destroy-method属性指定的初始化方法
    public void myDestory() {
        System.out.println("【destroyMethod()】执行 自定义Destory 方法。备注:通过<bean>的destroy-method属性指定的初始化方法");
    }
}
