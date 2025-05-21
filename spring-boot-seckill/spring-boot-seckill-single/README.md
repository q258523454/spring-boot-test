秒杀系统——（纯后台，单节点架构）
注意:这个是一个纯后台的springboot项目,没有前端
1.先执行 InitDataController 初始化user
2.用jmeter进行压测 /seckill/do_seckill

主要技术点:  
1.请求限流  
2.本地缓存  
3.redis缓存
4.mq异步消费

代码有详细注释
先看秒杀接口(com.sec.controller.SkOrderDoController.doSeckill)
然后看MQ消费接口(com.sec.rabbitmq.consumer.DirectConsumer.consumer)


流程:
准备工作:
启动缓存库存到 redis
内存标记商品是否售罄 goodsOverMap (数据库实际库存标记,可以用来断定库存为0)
执行流程:
1.限流 RateLimiter
2.内存 判断是否售罄 goodsOverMap
3.redis 判断是否重复秒杀 user_id:good_id
4.redis 库存减1, 若 库存为负数(不能说明数据库库存不足), 则判断 内存 goodsOverMap 标记是否售罄.
若内存标记售罄(数据库库存为0),则直接退出.否则查询数据库库存是否充足, 不足则标记 goodsOverMap 售罄.
库存还充足,才会发送到MQ
消费端消费:
1.查询db,判断实际库存
2.判断是否重复秒杀 user_id:good_id
3.减库存,写订单 (同一个事务下), 若库存扣减不成功, 表示库存不足


TODO：  
1.redis序列化方式  
2.升级为分布式系统


参考 GitHub: https://github.com/zaiyunduan123/springboot-seckill