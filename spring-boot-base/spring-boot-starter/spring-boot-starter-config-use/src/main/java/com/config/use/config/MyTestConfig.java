package com.config.use.config;

import com.config.use.util.SpringContextHolder;
import com.example.use0.entity.MatchIfMissingFalse;
import com.example.use0.entity.MatchIfMissingTrue;
import com.example.use1_base.conditionabean.ConditionalBean1;
import com.example.use1_base.conditionabean.ConditionalBean2;
import com.example.use1_base.config.AutoConfig1;
import com.example.use2_selector_bean.config.SelectBeanConfig1;
import com.example.use3_selector_bean2.config.SelectBeanConfig2;
import com.example.use3_selector_bean2.entity.MyProperties;
import com.example.use3_selector_bean2.entity.SelectBean1;
import com.example.use3_selector_bean2.entity.SelectBean2;
import com.example.use4_nested_properties.config.DatabaseAutoConfig;
import com.example.use4_nested_properties.entity.AllInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;


/**
 * @Description
 * @date 2020-04-02 9:50
 * @modify
 */

@Configuration
@DependsOn("springContextHolder")
@Slf4j
public class MyTestConfig {

    @Autowired(required = false)
    private AutoConfig1 mAutoConfig1;

    @PostConstruct
    public void init() {
        log.info("--------------------------- PostConstruct 内部配置 初始化 BEGIN ---------------------------");

        printBean(AutoConfig1.class);
        printBean(MatchIfMissingFalse.class);
        printBean(MatchIfMissingTrue.class);
        printBean(SelectBeanConfig1.class);
        printBean(SelectBeanConfig1.Cglib.class);
        printBean(SelectBeanConfig1.Jdk.class);
        printBean(ConditionalBean1.class);
        printBean(ConditionalBean2.class);

        // use3_selector_bean2
        log.info("-----------------------------------");
        printBean(MyProperties.class);
        printBean(SelectBeanConfig2.class);
        printBean(SelectBean1.class);
        printBean(SelectBean2.class);
        log.info("-----------------------------------");

        // use4_nested_properties
        printBean(AllInfo.class);
        printBean(DatabaseAutoConfig.class);
    }


    /**
     * 打印对应Class的Bean是否注入到容器
     */
    public <T> void printBean(Class<T> requiredType) {
        T instance = null;
        try {
            instance = SpringContextHolder.getBean(requiredType);
        } catch (Exception ex) {
            log.warn("bean:" + requiredType.getSimpleName() + " is null");
        }
        log.info(requiredType.getSimpleName() + ":{}", instance != null ? instance.toString() : "null");
    }
}
