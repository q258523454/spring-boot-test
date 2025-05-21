
package com.sharding5multidb.config.standard;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;


@Slf4j
public class MyTablePreciseAlgorithm implements StandardShardingAlgorithm<Integer> {

    private Properties props;

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        log.info("分表 collection:{}", JSON.toJSONString(availableTargetNames));
        String columnName = shardingValue.getColumnName();
        if (!columnName.equals("id")) {
            throw new RuntimeException("分片字段必须为 id ");
        }
        Integer value = shardingValue.getValue();

        String table = "";
        // 偶数用0号表,奇数用1号表
        if (value % 2 == 0) {
            table = (String) availableTargetNames.toArray()[0];
        } else {
            table = (String) availableTargetNames.toArray()[1];
        }
        log.info("分表 choose:{}", JSON.toJSONString(table));
        return table;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Integer> shardingValue) {
        return null;
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
