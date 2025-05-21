package com.order.config;

import com.order.entity.AutoConfigOrderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * AutoConfigureOrder: 注解表示作为外部依赖的时候, auto config(spring.factories) 加载的顺序, 注意在当前工程中无作用
 */
@Configuration
@ConditionalOnClass(AutoConfigOrderBean.class)
@AutoConfigureOrder(1)
public class AutoConfigOrder2 {
    private static final Logger logger = LoggerFactory.getLogger(AutoConfigOrder2.class);

    public AutoConfigOrder2() {
        logger.info(" AutoConfigOrder2 order 值:1 开始 初始化.");
        AutoConfigOrderBean.setName("AutoConfigOrder2 order 值:1");

    }
}
