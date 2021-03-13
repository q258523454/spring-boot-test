package com.example.entity;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "single3")
// 1.允许属性值为空, spring.factories 默认就是实例化
//   即:single3整个属性为空的时候,由spring.factories初始化bean
// 2.如果属性不为空, 必须 single3.open 属性为 true的时候才实例化, 此时 spring.factories不起作用了
//   即:当single3不为空的时候, 属性 open:true才初始化bean, 否则不初始化为bean (注意:此时spring.factories无效了,会被该配置属性覆盖)
@ConditionalOnProperty(prefix = "single3", value = "open", havingValue = "true", matchIfMissing = true)
public class SingleEntity3 {
    private String zhang = "default-single3";
    private boolean open;
}
