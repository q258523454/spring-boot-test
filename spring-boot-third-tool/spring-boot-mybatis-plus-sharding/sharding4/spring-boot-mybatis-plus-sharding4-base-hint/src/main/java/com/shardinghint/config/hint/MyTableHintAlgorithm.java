package com.shardinghint.config.hint;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;


import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;

@Slf4j
public class MyTableHintAlgorithm implements HintShardingAlgorithm<Integer> {


    /**
     * collection 是配置文件设置的可用 数据库名/数据表名
     * Hint数据库算法的时候就是 所有分库名集合
     * Hint数据表算法的时候就是 所有分表名集合
     * <p>
     * hintShardingValue 是分库分表的值
     * hintShardingValue.getValues() 值等于:
     * 1.分库hint: hintManager.addDatabaseShardingValue() 添加的集合
     * 2.分表hint: hintManager.addTableShardingValue() 添加的集合
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<Integer> shardingValue) {
        log.info("分表 collection:{}", JSON.toJSONString(availableTargetNames));
        log.info("分表 choose:{}", JSON.toJSONString(availableTargetNames));
        return availableTargetNames;
    }
}
