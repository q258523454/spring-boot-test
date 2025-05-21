package com.sharding5multidb.config.shardingsphere.rules;

import org.apache.shardingsphere.sharding.spi.ShardingAuditAlgorithm;
import org.apache.shardingsphere.spring.boot.registry.AbstractAlgorithmProvidedBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

/**
 * Sharding audit algorithm provided bean registry.
 */
public final class MyShardAuditAlgorithmRegistry extends AbstractAlgorithmProvidedBeanRegistry<ShardingAuditAlgorithm> {
    
    private static final String AUDITORS = "my.shardingsphere.rules.sharding.auditors.";
    
    public MyShardAuditAlgorithmRegistry(final Environment environment) {
        super(environment);
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) {
        registerBean(AUDITORS, ShardingAuditAlgorithm.class, registry);
    }
}