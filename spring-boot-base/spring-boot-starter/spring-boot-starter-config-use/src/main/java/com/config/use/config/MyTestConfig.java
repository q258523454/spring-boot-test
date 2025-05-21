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

@Configuration
@Slf4j
public class MyTestConfig {

    @Autowired(required = false)
    private AutoConfig1 mAutoConfig1;

    @PostConstruct
    public void init() {
        log.info("--------------------------- PostConstruct 内部配置 初始化 BEGIN ---------------------------");

        SpringContextHolder.printBean(AutoConfig1.class);
        SpringContextHolder.printBean(MatchIfMissingFalse.class);
        SpringContextHolder.printBean(MatchIfMissingTrue.class);
        SpringContextHolder.printBean(SelectBeanConfig1.class);
        SpringContextHolder.printBean(SelectBeanConfig1.Cglib.class);
        SpringContextHolder.printBean(SelectBeanConfig1.Jdk.class);
        SpringContextHolder.printBean(ConditionalBean1.class);
        SpringContextHolder.printBean(ConditionalBean2.class);

        // use3_selector_bean2
        log.info("-----------------------------------");
        SpringContextHolder.printBean(MyProperties.class);
        SpringContextHolder.printBean(SelectBeanConfig2.class);
        SpringContextHolder.printBean(SelectBean1.class);
        SpringContextHolder.printBean(SelectBean2.class);
        log.info("-----------------------------------");

        // use4_nested_properties
        SpringContextHolder.printBean(AllInfo.class);
        SpringContextHolder.printBean(DatabaseAutoConfig.class);
    }
}
