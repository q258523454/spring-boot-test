package com.sharding5multidb.config.shardingsphere.rules;

import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.apache.shardingsphere.spring.boot.registry.AbstractAlgorithmProvidedBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

public final class MyAlgorithmBeanRegistry extends AbstractAlgorithmProvidedBeanRegistry<ShardingAlgorithm> {
    
    private static final String SHARDING_ALGORITHMS = "my.shardingsphere.rules.sharding.sharding-algorithms.";
    
    public MyAlgorithmBeanRegistry(final Environment environment) {
        super(environment);
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) {
        registerBean(SHARDING_ALGORITHMS, ShardingAlgorithm.class, registry);
    }
}