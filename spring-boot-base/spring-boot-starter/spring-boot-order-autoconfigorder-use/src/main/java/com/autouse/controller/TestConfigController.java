package com.autouse.controller;


import com.autouse.service.OrderService;
import com.order.entity.AutoConfigOrderBean;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.annotation.PostConstruct;

/**
 * @Description
 * @date 2020-05-14 16:48
 * @modify
 */

@RestController
@DependsOn("springContextHolder")
@Slf4j
public class TestConfigController {
    /**
     * {@link EnableAutoConfiguration}
     * 总体执行顺序:
     * 1.内部 EnableAutoConfiguration(spring.factories)
     * 2.@PostConstruct
     * 3.外部 EnableAutoConfiguration(spring.factories)
     * <p>
     * {@link Order}
     * 只影响切面执行顺序
     * 将多个 OrderService bean 注入到 一个List, Order(x)就是集合顺序
     * 特别注意:ApplicationContext.getBeansOfType(AutoConfigService.class) 是不会按注入顺序返回的
     * <p>
     * {@link AutoConfigureOrder}
     * 只改变spring.factories（内+外）中的bean加载的顺序，跟本地项目中的bean加载顺序没关系
     */

    @Autowired
    private List<OrderService> orderServiceList;

    @PostConstruct
    public void init() {

        log.info("--------------------------- PostConstruct BEGIN ---------------------------");

        // spring.factories中的 AutoConfigOrderBean 并没有初始化, 因为它没有被yml配置触发, 将在本地项目所有Bean初始化之后再初始化
        log.info("AutoConfigOrderBean.name:{}", AutoConfigOrderBean.name);

        // @Order 只会影响注入List的顺序, 即 切面AOP 顺序, 与加载顺序无关
        log.info("--------------------------- 注入List顺序 ---------------------------");
        for (OrderService orderBean : orderServiceList) {
            log.info("--------------" + orderBean.getClass().getSimpleName() + ",  Order=" + orderBean.getClass().getAnnotation(Order.class).value() + "--------------");
        }

        log.info("--------------------------- PostConstruct END ---------------------------");
    }
}
