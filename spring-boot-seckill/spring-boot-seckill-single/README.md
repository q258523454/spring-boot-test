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

TODO：  
1.redis序列化方式  
2.升级为分布式系统  

参考 GitHub: https://github.com/zaiyunduan123/springboot-seckill   