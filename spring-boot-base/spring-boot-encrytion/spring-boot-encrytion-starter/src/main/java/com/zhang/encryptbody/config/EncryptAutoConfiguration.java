package com.zhang.encryptbody.config;


import com.zhang.encryptbody.advice.DecryptRequestBodyAdvice;
import com.zhang.encryptbody.advice.EncryptResponseBodyAdvice;
import com.zhang.encryptbody.pojo.properties.EncryptConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@Configuration
@EnableConfigurationProperties(EncryptConfig.class)
@ConditionalOnProperty(prefix = "lj08.encrypt", value = "open", havingValue = "true", matchIfMissing = false)
public class EncryptAutoConfiguration {

    public EncryptAutoConfiguration() {
        log.info("EncryptAutoConfiguration Constructor");
    }

    /**
     * 减少耦合设置  matchIfMissing = false, 表示不被 spring.factories 启动
     * 因此当且仅当 encrypt.open:true 的时候, 才会注入以下bean
     * {@link EncryptAutoConfiguration}
     * {@link EncryptResponseBodyAdvice}
     * {@link DecryptRequestBodyAdvice}
     * 没有配置或者配置属性不正确都不会注入bean, 减少对代码的侵入
     */

    @Bean
    public EncryptResponseBodyAdvice encryptResponseBodyAdvice() {
        return new EncryptResponseBodyAdvice();
    }

    @Bean
    public DecryptRequestBodyAdvice decryptRequestBodyAdvice() {
        return new DecryptRequestBodyAdvice();
    }
}
