# kafka demo

## idea模拟启动多个pod

修改VM参数:   
-Dserver.port=8080  
-Dserver.port=8081

## 实际测试

假设有1个group,绑定了2个topic,分别是demo-topic-1(分区3)、demo-batch-topic-1(分区3)
修改'-Dserver.port=8082'来启动多个pod模拟集群

### topic分区=3, concurrency=1, pod=2

启动的时候kafka会自动分配分区

```
pod1: 被指定了如下分区
demo-group: partitions assigned: [demo-topic-1, demo-topic-0]
demo-group: partitions assigned: [demo-batch-topic-2]

pod2: 被指定了如下分区
demo-group: partitions assigned: [demo-topic-2]
demo-group: partitions assigned: [demo-batch-topic-1, demo-batch-topic-0]
```

每次produce发送消息,会指定到某个分区  
例如:  
向demo-batch-topic发送的时候，partition=2 会到pod1消费  
向demo-batch-topic发送的时候，partition=0或1 会到pod1消费

### topic分区=3, concurrency=3, pod=3

即使指定单个pod用多个线程,但是kafka会尽可能的跨实例分配

```
pod1:
demo-group: partitions assigned: [demo-batch-topic-1]
demo-group: partitions assigned: [demo-topic-1]

pod2:
demo-group: partitions assigned: [demo-topic-2]
demo-group: partitions assigned: [demo-batch-topic-0]

pod3:
demo-group: partitions assigned: [demo-topic-0]
demo-group: partitions assigned: [demo-batch-topic-2]
```

## 场景举例

```
topic最大分片=3,concurrency=1
pod=1,concurrency=1: 总线程数1,相当于单线程消费3个分片
pod=2,concurrency=1: 总线程数2,相当于2个pod共同消费3个分片,其中1个pod单线程消费2个分片
pod=3,concurrency=1: 总线程数3,相当于3个pod共同消费3个分片,每个pod有1个线程消费1个分片（完美负载）
pod=4,concurrency=1: 有1个pod不会去消费（可以提升高可用,当其他pod挂掉后会接替——故障转移）

topic最大分片=3,concurrency=2
pod=2,concurrency=2: 总线程数4,其中1个pod的1个线程闲置

topic最大分片=3,concurrency=3
pod=3,concurrency=3: 总线程数9,kafka会尽量按pod均匀分配,理论上每个pod有1个线程消费1个分区(实际测试确实如此)
核心:看总线程数
```

## 总结

```
Pod × concurrency < 分区数: 部分pod的单个线程会同时消费多个分区,导致性能变慢
Pod × concurrency = 分区数: 每个分区都有单独线程处理
Pod × concurrency > 分区数: 多余线程会空闲，浪费资源

生产环境下的完美负载：当 concurrency=1, pod数>=分区, 此时每个分区会单独消费一个分区
```

## 其他

### @PreDestroy模拟

```
http://localhost:8080/shutdown 来触发
```


