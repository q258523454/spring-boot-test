package com.example.use4_nested_properties.config;


import com.example.use4_nested_properties.entity.AllInfo;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@EnableConfigurationProperties(AllInfo.class)
@ConditionalOnProperty(prefix = "my4.datasource", value = "open", havingValue = "true", matchIfMissing = false)
public class DatabaseAutoConfig {

    /**
     * TODO: 如果有bean需要先配置属性, 可以在这里写  @Bean, 例如
     * @Bean
     * public Abc abc() {
     *     return new Abc();
     * }
     * 这样就能实现 bean 的注入前提是需要配置生效（动态注入）
     */

}
