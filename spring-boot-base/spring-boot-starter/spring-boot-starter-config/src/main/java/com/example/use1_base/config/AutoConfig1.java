package com.example.use1_base.config;

import com.example.use1_base.service.AutoConfigService;
import com.example.use1_base.service.impl.AutoConfigServiceImpl;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
 * {@link Configuration} 相当于xml配置bean
 *
 * {@link ConditionalOnClass} 生效条件
 * 当 AutoConfigService 在类路径[com.example.service.AutoConfigService]的条件下AutoConfig才会实例化
 *
 * {@link EnableConfigurationProperties} 实例化对应配置,即:将配置文件中的对应属性赋值到对应的类,并注入成bean
 * 就是让{@link org.springframework.boot.context.properties.ConfigurationProperties} 生效
 * 此时会将 application.properties 的相关的属性字段与该类一一对应，并生成 MyAutoProperties Bean
 * EnableConfigurationProperties可以看成:@ConfigurationProperties+@Component
 * 总结:
 * {@link AutoConfig1}本身没有 @Component 注解,是不会自动注入成bean的,
 * 因此需要加@EnableConfigurationProperties，来让其注入成bean
 *
 * {@link ConditionalOnProperty} 检查是否有指定的属性, 或者指定属性是否有指定的值,prefix 可以跟 {@link MyAutoProperties} 属性不一样
 * matchIfMissing = false: 必须有该配置项和属性值,且必须等于havingValue(默认为""), 否则不实例化, 默认是 false
 * matchIfMissing = true : 可以没有该配置项和属性值(此时spring.factories来默认实例化),但非空时候必须等于havingValue,否则不实例化
 * havingValue表示 当 prefix.value 相等的时候, 配置才会实例化  默认 havingValue = ""
 *
 * {@link ConditionalOnMissingBean} 作用:当缺失某个bean时会初始化bean
 */
@Data
@Configuration
@ConditionalOnClass(AutoConfigService.class)
@EnableConfigurationProperties(MyAutoProperties.class)
@ConditionalOnProperty(prefix = "my1", name = "open", havingValue = "true", matchIfMissing = false)
public class AutoConfig1 {
    private static final Logger logger = LoggerFactory.getLogger(AutoConfig1.class);

    public AutoConfig1() {
        logger.info("AutoConfig1: 初始化.");
    }

    /**
     * ConditionalOnMissingBean(AutoConfigService.class)表示bean满足以下条件才会注入:
     * 1.外层 MyAutoConfig bean 正常注入
     * 2.AutoConfigService没有具体实现类(可以避免项目无法启动或存在冲突)
     */
    @Bean
    @ConditionalOnMissingBean(AutoConfigService.class)
    public AutoConfigService autoConfigService1() {
        logger.info("-------------------- ConditionalOnMissingBean： AutoConfigService  -----------------");
        AutoConfigServiceImpl autoConfigService = new AutoConfigServiceImpl();
        autoConfigService.print();
        return autoConfigService;
    }

}
