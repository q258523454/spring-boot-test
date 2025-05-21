
Sharding-jdbc 分表策略  
  标准分片策略(standard) : 单个字段
    PreciseShardingAlgorithm（精准分片） : 必须实现,用于 SQL 含有 = 和 in 的分片处理  
    RangeShardingAlgorithm（范围分片）: 范围分片算法是非必选的，用于处理含有 between and 的分片处理  
    注意: 一旦我们没配置范围分片算法，而 SQL 中又用到 between and 或者 like等，那么 SQL 将按全库、全表 逐一路由执行，查询性能会很差需要特别注意  
  
  复合分片策略(complex) : 多个字段
  
  行表达式分片策略(inline) : 单个字段，最简单的分片策略
  
  Hint分片策略(Hint) :  无需配置分片字段, 自定义分片字段, 通过 Hint API 手工指定. 例如: 希望 teacher 表按照 student.id 字段进行分片.