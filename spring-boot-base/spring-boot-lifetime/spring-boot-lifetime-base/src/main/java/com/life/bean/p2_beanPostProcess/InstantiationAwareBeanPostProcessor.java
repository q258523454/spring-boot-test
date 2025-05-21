package com.life.bean.p2_beanPostProcess;

import com.life.entity.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;


/**
 * 本质是BeanPostProcessor的子接口
 */
@Component
public class InstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {
    public InstantiationAwareBeanPostProcessor() {
        super();
        System.out.println("【InstantiationAwareBeanPostProcessor】 构造函数");
    }

    /**
     * 实例化Bean之前调用
     * 实例化: 是对象创建的过程。比如使用构造方法new对象，为对象在内存中分配空间。
     * 初始化: 是为对象中的属性赋值的过程。
     */
    @Override
    public Object postProcessBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
        if (null != beanName && !beanName.isEmpty() && beanName.equals("person")) {
            System.out.println("【InstantiationAwareBeanPostProcessor】-postProcessBeforeInstantiation:" + beanName + " 实例化bean之前");
        }
        return null;
    }


    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (null != beanName && !beanName.isEmpty() && "person".equals(beanName)) {
            System.out.println("【InstantiationAwareBeanPostProcessor】-postProcessAfterInstantiation: beanName=" + beanName + " 实例化bean之后");
        }
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    // 接口方法、设置某个属性时调用
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        if (null != beanName && !beanName.isEmpty() && "person".equals(beanName)) {
            System.out.println("【InstantiationAwareBeanPostProcessor】-postProcessPropertyValues: beanName=" + beanName + " 开始属性注入.");
            System.out.println("修改前：Person()name=" + ((Person) bean).getName() + ",age=" + ((Person) bean).getAge());
            System.out.println("开始修改属性");
            ((Person) bean).setAge(2);
            System.out.println("修改后：Person()name=" + ((Person) bean).getName() + ",age=" + ((Person) bean).getAge());
        }
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (null != beanName && !beanName.isEmpty() && "person".equals(beanName)) {
            System.out.println("【InstantiationAwareBeanPostProcessor】-BeforeInitialization beanName:" + beanName + " 初始化bean之前");
        }
        return super.postProcessBeforeInitialization(bean, beanName);
    }


    /**
     * 初始化Bean之后调用
     * 实例化: 是对象创建的过程。比如使用构造方法new对象，为对象在内存中分配空间。
     * 初始化: 是为对象中的属性赋值的过程。
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (null != beanName && !beanName.isEmpty() && "person".equals(beanName)) {
            System.out.println("【InstantiationAwareBeanPostProcessor】-AfterInitialization beanName:" + beanName + "初始化bean之后)");
        }
        return bean;
    }
}
