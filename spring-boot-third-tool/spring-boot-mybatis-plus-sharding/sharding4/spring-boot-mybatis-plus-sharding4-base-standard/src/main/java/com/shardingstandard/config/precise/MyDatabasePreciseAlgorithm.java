package com.shardingstandard.config.precise;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

@Slf4j
public class MyDatabasePreciseAlgorithm implements PreciseShardingAlgorithm<Integer> {

    /**
     * 库分片(precise策略),对应配置:
     * spring.shardingsphere.sharding.tables.student.database-strategy.standard.precise-algorithm-class-name
     * <p>
     * availableTargetNames 是配置文件设置的可用 数据库名/数据表名
     * 库分片时候就是 所有分库名集合
     * 表分片时候就是 所有分表名集合
     * <p>
     * shardingValue 分片属性{逻辑表、分片字段、分片值}
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        log.info("分库 collection:{}", JSON.toJSONString(availableTargetNames));

        String columnName = shardingValue.getColumnName();
        if (!columnName.equals("id")) {
            throw new RuntimeException("分片字段必须为 id ");
        }
        Integer value = shardingValue.getValue();

        String db = "";
        // student.id: 偶数用0号库,奇数用1号库
        if (value % 2 == 0) {
            db = (String) availableTargetNames.toArray()[0];
        } else {
            db = (String) availableTargetNames.toArray()[1];
        }
        String dataSource = (String) availableTargetNames.toArray()[0];
        log.info("分库 choose :{}", db);
        return db;
    }

}
