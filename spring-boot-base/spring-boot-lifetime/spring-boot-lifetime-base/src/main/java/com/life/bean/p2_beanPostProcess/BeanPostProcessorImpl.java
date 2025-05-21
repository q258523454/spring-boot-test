package com.life.bean.p2_beanPostProcess;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;


/**
 * 容器级：工厂后处理器接口
 */
@Component
public class BeanPostProcessorImpl implements BeanPostProcessor {

    public BeanPostProcessorImpl() {
        super();
        System.out.println("【BeanPostProcessor】 构造函数");
    }

    //  第一个参数都是要处理的Bean对象，第二个参数都是Bean的name
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (null != s && !s.isEmpty() && s.equals("person")) {
            System.out.println("【BeanPostProcessor】-BeforeInitialization: Bean 属性修改(初始化)之前");
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (null != s && !s.isEmpty() && s.equals("person")) {
            System.out.println("【BeanPostProcessor】-AfterInitialization: Bean 属性修改(初始化)之后");
        }
        return o;
    }
}