package com.sharding5multidb.config.shardingsphere.rules;

import org.apache.shardingsphere.sharding.spring.boot.condition.ShardingSpringBootCondition;
import org.apache.shardingsphere.spring.boot.util.PropertyUtil;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;


/**
 * 参考
 * {@link ShardingSpringBootCondition}
 */
public final class MyShardingSpringBootCondition extends SpringBootCondition {
    
    private static final String SHARDING_PREFIX = "my.shardingsphere.rules.sharding";
    
    @Override
    public ConditionOutcome getMatchOutcome(final ConditionContext conditionContext, final AnnotatedTypeMetadata annotatedTypeMetadata) {
        return PropertyUtil.containPropertyPrefix(conditionContext.getEnvironment(), SHARDING_PREFIX)
                ? ConditionOutcome.match()
                : ConditionOutcome.noMatch("Can't find ShardingSphere sharding rule configuration in local file.");
    }
}