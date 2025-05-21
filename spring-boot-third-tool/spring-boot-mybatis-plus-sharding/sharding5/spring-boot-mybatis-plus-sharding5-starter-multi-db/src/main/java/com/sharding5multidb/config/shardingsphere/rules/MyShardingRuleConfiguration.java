
package com.sharding5multidb.config.shardingsphere.rules;

import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.apache.shardingsphere.sharding.spi.ShardingAuditAlgorithm;
import org.apache.shardingsphere.sharding.spring.boot.ShardingRuleSpringBootConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.YamlShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.yaml.swapper.YamlShardingRuleAlgorithmProviderConfigurationSwapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;


/**
 * 参考
 * {@link ShardingRuleSpringBootConfiguration}
 */
@Configuration
@ConditionalOnClass(YamlShardingRuleConfiguration.class)
@Conditional(MyShardingSpringBootCondition.class)
public class MyShardingRuleConfiguration {

    private final YamlShardingRuleAlgorithmProviderConfigurationSwapper swapper = new YamlShardingRuleAlgorithmProviderConfigurationSwapper();

    @Autowired
    private MyYamlShardingRuleProps yamlConfig;

    /**
     * Create sharding rule configuration bean.
     *
     * @param shardingAlgorithmProvider      sharding algorithm provider
     * @param keyGenerateAlgorithmProvider   key generate algorithm provider
     * @param shardingAuditAlgorithmProvider sharding audit algorithm provider
     * @return sharding rule configuration
     */
    @Bean
    public RuleConfiguration shardingRuleConfiguration(final ObjectProvider<Map<String, ShardingAlgorithm>> shardingAlgorithmProvider,
            final ObjectProvider<Map<String, KeyGenerateAlgorithm>> keyGenerateAlgorithmProvider,
            final ObjectProvider<Map<String, ShardingAuditAlgorithm>> shardingAuditAlgorithmProvider) {
        Map<String, ShardingAlgorithm> shardingAlgorithmMap = Optional.ofNullable(shardingAlgorithmProvider.getIfAvailable()).orElse(Collections.emptyMap());
        Map<String, KeyGenerateAlgorithm> keyGenerateAlgorithmMap = Optional.ofNullable(keyGenerateAlgorithmProvider.getIfAvailable()).orElse(Collections.emptyMap());
        Map<String, ShardingAuditAlgorithm> shardingAuditAlgorithmMap = Optional.ofNullable(shardingAuditAlgorithmProvider.getIfAvailable()).orElse(Collections.emptyMap());
        AlgorithmProvidedShardingRuleConfiguration result = swapper.swapToObject(yamlConfig.getSharding());
        result.setShardingAlgorithms(shardingAlgorithmMap);
        result.setKeyGenerators(keyGenerateAlgorithmMap);
        result.setAuditors(shardingAuditAlgorithmMap);
        return result;
    }

    /**
     * Create sharding algorithm provided bean registry.
     *
     * @param environment environment
     * @return sharding algorithm provided bean registry
     */
    @Bean
    public static MyAlgorithmBeanRegistry shardingAlgorithmProvidedBeanRegistry(final Environment environment) {
        return new MyAlgorithmBeanRegistry(environment);
    }

    /**
     * Create key generator algorithm provided bean registry.
     *
     * @param environment environment
     * @return key generator algorithm provided bean registry
     */
    @Bean
    public static MyKeyGenerateAlgorithmRegistry keyGenerateAlgorithmProvidedBeanRegistry(final Environment environment) {
        return new MyKeyGenerateAlgorithmRegistry(environment);
    }

    /**
     * Create sharding auditor algorithm provided bean registry.
     *
     * @param environment environment
     * @return sharding auditor algorithm provided bean registry
     */
    @Bean
    public static MyShardingAuditAlgorithmRegistry shardingAuditAlgorithmProvidedBeanRegistry(final Environment environment) {
        return new MyShardingAuditAlgorithmRegistry(environment);
    }
}
