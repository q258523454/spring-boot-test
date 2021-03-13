package com.order.config;

import com.order.entity.AutoConfigOrderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @date 2020-04-02 10:55
 * @modify
 */
@Configuration
/**
 * 当 {@link com.order.entity.AutoConfigOrderBean} 存在
 * AutoConfigOrder1 生效
 */
@ConditionalOnClass(AutoConfigOrderBean.class)
// 注解表示作为外部依赖的时候, auto config(spring.factories) 加载的顺序, 注意在当前工程中无作用
@AutoConfigureOrder(2)
public class AutoConfigOrder1 {
    private static
final Logger logger = LoggerFactory.getLogger(AutoConfigOrder1.class);
    public AutoConfigOrder1() {
        logger.info(" AutoConfigOrder1 order 值:2 开始 初始化.");
        AutoConfigOrderBean.setName("AutoConfigOrder1 order 值:2");
    }
}
