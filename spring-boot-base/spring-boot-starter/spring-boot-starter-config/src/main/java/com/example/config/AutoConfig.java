package com.example.config;

import com.example.entity.AutoConfigEntity;
import com.example.service.AutoConfigService;
import com.example.service.impl.AutoConfigServiceImpl;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhangj
 * @Date: 2019-09-11
 * @Version 1.0
 */


/**
 * @Configuration 相当于xml配置bean
 *
 * @ConditionalOnClass 生效条件
 * 当 AutoConfigService 在类路径[com.example.service.AutoConfigService]的条件下AutoConfig才生效(条件之一)
 *
 * @EnableConfigurationProperties 让配置生效
 * 就是让{@link org.springframework.boot.context.properties.ConfigurationProperties} 生效
 * 此时会将 application.properties 的相关的属性字段与该类一一对应，并生成 AutoEntity Bean
 *
 * @ConditionalOnProperty 检查是否有指定的属性, 或者指定属性是否有指定的值
 * 当 matchIfMissing = false 时（默认），必须有属性值,且必须匹配时候配置才生效, 否则不实例化 AutoConfig, 默认是 false
 * 当 matchIfMissing = true ,允许属性值为空,但非空时候必须匹配havingValue配置才生效, 否则不实例化 AutoConfig,
 * havingValue表示 当 prefix.value 相等的时候, 配置才生效 默认 havingValue = "false"
 */
@Data
@Configuration
@ConditionalOnClass(AutoConfigService.class)
@EnableConfigurationProperties({AutoConfigEntity.class})
@ConditionalOnProperty(prefix = "my", value = "open", havingValue = "true", matchIfMissing = false)
public class AutoConfig {
    private static final Logger logger = LoggerFactory.getLogger(AutoConfig.class);

    @Autowired
    private AutoConfigEntity autoConfigEntity;

    public AutoConfig() {
        logger.info("AutoConfig 开始 初始化.");
    }

    /**
     * @ConditionalOnMissingBean 表示当缺失该bean时创建并初始化, 这样可以避免项目启动冲突
     */
    @Bean
    @ConditionalOnMissingBean(AutoConfigService.class)
    public AutoConfigService autoConfigService() {
        logger.info("-------------------- 默认初始化: initialize bean AutoConfigService -----------------");
        AutoConfigServiceImpl autoConfigService = new AutoConfigServiceImpl();
        autoConfigService.print();
        return autoConfigService;
    }
}
