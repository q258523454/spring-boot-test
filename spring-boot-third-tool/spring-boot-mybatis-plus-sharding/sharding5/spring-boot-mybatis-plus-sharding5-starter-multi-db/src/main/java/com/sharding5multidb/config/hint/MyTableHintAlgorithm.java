package com.sharding5multidb.config.hint;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

import java.util.Collection;
import java.util.Properties;

@Slf4j
public class MyTableHintAlgorithm implements HintShardingAlgorithm<Integer> {

    private Properties props;

    @Override
    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<Integer> hintShardingValue) {
        log.info("分表 collection:{}", JSON.toJSONString(collection));
        log.info("分表 choose:{}", JSON.toJSONString(collection));
        return collection;
    }

    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void init(Properties props) {
        this.props = props;
    }
}
