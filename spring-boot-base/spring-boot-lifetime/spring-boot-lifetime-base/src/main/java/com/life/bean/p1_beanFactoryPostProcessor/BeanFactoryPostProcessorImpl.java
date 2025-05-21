package com.life.bean.p1_beanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;


/**
 * spring在读取beanDefinition信息之后，实例化bean之前。
 */
@Component
public class BeanFactoryPostProcessorImpl implements BeanFactoryPostProcessor {

    public BeanFactoryPostProcessorImpl() {
        super();
        System.out.println("【BeanFactoryPostProcessor】 构造器");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // BeanFactoryPostProcessor这里赋值，在bean没有初始化之前?
        BeanDefinition bd = configurableListableBeanFactory.getBeanDefinition("person");
        String name = (String) bd.getPropertyValues().get("name");
        Integer age = (Integer) bd.getPropertyValues().get("age");
        System.out.println("【BeanFactoryPostProcessor】");
        System.out.println("person：name=" + name + ",age=" + age + "");
        System.out.println("修改Person.age的值为:99, setter注入(xml配置), 优先级高于构造函数等注入");
        bd.getPropertyValues().addPropertyValue("age", "99");
    }


}
