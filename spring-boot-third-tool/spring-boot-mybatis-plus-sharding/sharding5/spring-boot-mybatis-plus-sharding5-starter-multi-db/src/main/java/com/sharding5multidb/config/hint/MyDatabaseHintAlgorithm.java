package com.sharding5multidb.config.hint;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class MyDatabaseHintAlgorithm implements HintShardingAlgorithm<Integer> {

    private Properties props;

    /**
     * 固定选第一个分库
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<Integer> hintShardingValue) {
        log.info("分库 collection:{}", JSON.toJSONString(collection));
        String dataSource = (String) collection.toArray()[0];
        log.info("分库 choose :{}", dataSource);
        return Collections.singleton(dataSource);
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
