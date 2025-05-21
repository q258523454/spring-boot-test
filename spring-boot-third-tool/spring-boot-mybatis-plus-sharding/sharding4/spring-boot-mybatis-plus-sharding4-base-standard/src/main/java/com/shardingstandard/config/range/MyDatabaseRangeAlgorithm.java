package com.shardingstandard.config.range;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;

@Slf4j
public class MyDatabaseRangeAlgorithm implements RangeShardingAlgorithm<Integer> {

    /**
     * 库分片(range策略)
     *
     * 如果配置了 Range 策略, 会走对应的策略
     * 如果只配置 precise 策略 , 则按照 全库、全表 逐一路由执行全部
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Integer> shardingValue) {
        log.info("分库 collection:{}", JSON.toJSONString(availableTargetNames));

        String columnName = shardingValue.getColumnName();
        if (!columnName.equals("id")) {
            throw new RuntimeException("分片字段必须为 id ");
        }
        Integer lowerEndpoint = shardingValue.getValueRange().lowerEndpoint();
        Integer upperEndpoint = shardingValue.getValueRange().upperEndpoint();

        log.info("lowerEndpoint:{}", lowerEndpoint);
        log.info("upperEndpoint:{}", upperEndpoint);

        // 这里没有做处理, 路由全部库
        return availableTargetNames;
    }
}
