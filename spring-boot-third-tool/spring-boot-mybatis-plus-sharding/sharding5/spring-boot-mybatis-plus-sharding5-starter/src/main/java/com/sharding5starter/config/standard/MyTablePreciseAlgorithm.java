// package com.updateversion.config.standard;
//
// import com.alibaba.fastjson.JSON;
//
// import lombok.extern.slf4j.Slf4j;
//
// import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
// import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
// import java.util.Collection;
//
// @Slf4j
// public class MyTablePreciseAlgorithm implements PreciseShardingAlgorithm<Integer> {
//
//     /**
//      * 表分片(precise策略),对应配置:
//      * spring.shardingsphere.sharding.tables.student.table-strategy.standard.precise-algorithm-class-name
//      *
//      * Precise 策略
//      * student.id: 偶数用0号表,奇数用1号表
//      */
//     @Override
//     public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
//         log.info("分表 collection:{}", JSON.toJSONString(availableTargetNames));
//         String columnName = shardingValue.getColumnName();
//         if (!columnName.equals("id")) {
//             throw new RuntimeException("分片字段必须为 id ");
//         }
//         Integer value = shardingValue.getValue();
//
//         String table = "";
//         // 偶数用0号表,奇数用1号表
//         if (value % 2 == 0) {
//             table = (String) availableTargetNames.toArray()[0];
//         } else {
//             table = (String) availableTargetNames.toArray()[1];
//         }
//         log.info("分表 choose:{}", JSON.toJSONString(table));
//         return table;
//     }
// }
