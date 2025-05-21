package com.sharding5multidb.config.shardingsphere.rules;

import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;
import org.apache.shardingsphere.spring.boot.registry.AbstractAlgorithmProvidedBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

/**
 * Key generator algorithm provided bean registry.
 */
public final class MyKeyGenerateAlgorithmRegistry extends AbstractAlgorithmProvidedBeanRegistry<KeyGenerateAlgorithm> {
    
    private static final String KEY_GENERATORS = "my.shardingsphere.rules.sharding.key-generators.";
    
    public MyKeyGenerateAlgorithmRegistry(final Environment environment) {
        super(environment);
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) {
        registerBean(KEY_GENERATORS, KeyGenerateAlgorithm.class, registry);
    }
}