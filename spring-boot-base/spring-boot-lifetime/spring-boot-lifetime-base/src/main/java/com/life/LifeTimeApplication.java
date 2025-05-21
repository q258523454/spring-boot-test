package com.life;

import com.life.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({
        "com.life.**"
})
@Slf4j
public class LifeTimeApplication {
    /**
     * 一个bean创建要三步:
     *   1.实例化，简单理解就是new了一个对象
     *   2.属性注入,即 BeanPostProcessor-postProcessPropertyValues，为实例化中new出来的对象填充属性
     *   3.初始化,接 InitializingBean-afterPropertiesSet,执行aware接口中的方法，初始化方法，完成AOP代理
     *
     *  Bean级生命周期接口:
     *      1.BeanNameAware: 只有1个方法--setBeanName()
     *      2.BeanFactoryAware: 只有1个方法--setBeanFactory()
     *      3.InitializingBean: 只有1个方法--afterPropertiesSet(),xml中的init-method()其实就是调用这个方法
     *      4.DisposableBean: 只有1个方法--DisposableBean(),xml中的destroy-method()其实就是调用这个方法
     *  容器级Bean生命周期接口:
     *      1.BeanPostProcessor
     *      2.InstantiationAwareBeanPostProcessor(实际上继承自 BeanPostProcessor)
     *
     *  实例化(Instantiation): 是对象创建的过程。比如使用构造方法new对象，为对象在内存中分配空间。
     *  初始化(Initialization): 是为对象中的属性赋值的过程。
     *
     *  BeanFactoryPostProcessor
     *   -postProcessBeanFactory  : spring在读取beanDefinition信息之后，实例化bean之前
     *            由于AOP机制,这里修改的属性将直接影响初始化后的bean属性。
     *            因为它是在最先开始进入的切面,因此退出的也比较靠后。权限高于其他。
     *            主要用于覆盖bean属性等等
     *
     *  InstantiationAwareBeanPostProcessor
     *   -1.postProcessBeforeInstantiation   : 实例化bean之前,相当于 new Person()之前
     *      1.1.new Person()
     *   -2.postProcessAfterInstantiation   : 实例化bean之后,相当于 new Person()之后
     *   -3.postProcessPropertyValues     : 实例化bean完成,开始属性注入
     *      3.3.这里可以注入自定义属性
     *   -4.postProcessBeforeInitialization  : BeanPostProcessor-前置处理。
     *    bean实例化后,初始化之前,相当于将 bean 注入到 spring 容器上下文之前
     *    这里执行:下面的BeanNameAware,BeanFactoryAware,initMethod()
     *      4.1.BeanNameAware
     *       -setBeanName     : bean实例化后,初始化之前
     *      4.2.BeanFactoryAware
     *       -setBeanFactory    : bean实例化后,初始化之前. 可缓存BeanFactory,对每个bean作特殊化定制
     *      4.3.initMethod()
     *       执行顺序如下:
     *       4.3.1.@PostConstruct  : 可以用来初始化某个属性
     *       4.3.2.InitializingBean : afterPropertiesSet()
     *       4.3.3.init-method()  :1.xml<bean>的 init-method指定的初始化方法
     *              2.@Bean(initMethod = "myInit")指定
     *      4.4.destroyMethod()
     *      - 1.<bean>的 destory-method 属性指定的初始化方法
     *      - 2.@Bean(destroyMethod = "myDestory")指定
     *   -5.postProcessAfterInitialization   : BeanPostProcessor-后置处理。
     *       bean实例化后,初始化之后,相当于将 bean 注入到 spring 容器上下文之后
     */
    public static void main(String[] args) {
        SpringApplication.run(LifeTimeApplication.class, args);
    }

    // myInit 类似于 InitializingBean中的afterPropertiesSet
    @Bean(initMethod = "myInit", destroyMethod = "myDestory")
    public Person person() {
        System.out.println("准备使用构造器实例化 Person(张小凡,1)");
        return new Person("张小凡", 1);
    }

}