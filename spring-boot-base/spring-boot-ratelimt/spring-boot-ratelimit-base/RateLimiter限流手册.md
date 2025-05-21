# 限流使用

## 引入依赖包

```
<dependency>
    <groupId>xxxxxxx</groupId>
    <artifactId>ratelimiter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 支持类型
- 单例限流——Guava模式
- 分布式限流——Redis 计数器模式
- 分布式限流——Redis 令牌桶模式

## 功能支持
1. 支持Spel表达式
2. **支持动态修改限流策略**

## 使用教程
### 单例限流——Guava模式
#### 方法1：基本使用（一般用这个就行了）
```java
    /**
     * 在api或者方法上加注解,指定type和limit即可
     */
    @PostMapping(value = "/ratelimit/guava/dafault")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.GUAVA, limit = 5)
    public String test1(@RequestBody Student student, @RequestParam String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "index";
    }

```

#### 方法2：指定降级策略
```java
    /**
     * callBackMethod 指定降级策略
     * callBackMethod 指定命中限流策略后, 回调的方法（降级策略）, 不指定则会直接打印异常
     */
    @PostMapping(value = "/ratelimit/guava/callback")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.GUAVA, limit = 5, callBackMethod = "callBack1")
    public String test3(@RequestBody Student student, @RequestParam String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "index";
    }

    /**
     * 限流后降级处理方法,通过反射获取
     * 1.必须与限流方法在同个类下定义;
     * 2.入参必须与限流接口保持一致;
     */
    public void callBack1(Student student, String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        log.error("callBack1 execute");
    }
```
#### 方法3：SpEL表达式
```java
    /**
     * SpEL表达式, 动态指定限流key后缀
     * 注意:表达式不要为空, 同时要考虑SpEL的值不能为空, 为空则会忽略SpEL, 因此尽量保证SpEL一定有值.
     * 例如:这里根据指定 id 限流
     */
    @PostMapping(value = "/ratelimit/guava/spel")
    @RateLimiterAnnotation(spel = {"#student.id", "#student.age"}, type = RateLimiterTypeEnum.GUAVA, limit = 5)
    public String test4(@RequestBody Student student, @RequestParam String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "spel";
    }

```
#### 特殊应用：动态修改限流策略
```java
    /**
     * 动态修改限流策略
     * 通过 propeties 修改限流策略(配置读取,优先级最高)
     * 例如在application.properties新增配置:
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).limit=5
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).type=guava
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).spel=#student.age,#student.id
     * com.xxx.TestGuavaController#readProperties(com.xxx.Student,java.lang.String).callBackMethod=callBack2
     * 下面的注解值将全部被替换,实现动态修改
     */
    @PostMapping(value = "/ratelimit/guava/readproperties")
    @RateLimiterAnnotation(spel = {"#student.id"}, type = RateLimiterTypeEnum.GUAVA, limit = 100, callBackMethod = "callBack1")
    public String readProperties(@RequestBody Student student, @RequestParam String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "index";
    }

    public void callBack2(Student student, String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        log.error("callBack2 execute");
    }
```

通过以下方法可以获取要修改方法的key值(RateLimiter的唯一键值)
```java
com.ratelimitbase.utils.AopUtils#getFullyMethodPathWithParameter
```
使用方法: 例如我想获取  `TestGuavaController` 类下 `test1` 方法的限流key
```java
AopUtils.getFullyMethodPathWithParameter(TestGuavaController.class, "test1")
```
最后把上述值放到 `application.properties`, 假如上面`AopUtils.getFullyMethodPathWithParameter(TestGuavaController.class, "test1")`的值`com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String)`，那`application.properties`添加如下配置：
```
com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).limit=5 --- 【必须】
com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).type=guava --- 【必须】
com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).spel=#student.age,#student.id --- 【非必须】
com.xxx.TestGuavaController#test1(com.xxx.Student,java.lang.String).callBackMethod=callBack2 --- 【非必须】
```
上述值将替换对应注解的值,实现动态修改。注意配置中心如果是主动推送，无需重启。如果不是主动推送配置，需要重启pod。


### 分布式限流——Redis 计数器模式
#### 配置Redis (RedisTemplate)
```java
/**
 * Redis配置信息
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean("redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, String> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        // key 序列化方式: StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // value 序列化方式: Jackson2JsonRedisSerializer
        template.setValueSerializer(getJacksonSerializer());

        template.afterPropertiesSet();
        return template;
    }

    private Jackson2JsonRedisSerializer<Object> getJacksonSerializer() {
        // key序列化方式: Jackson2JsonRedisSerializer, 默认使用JDK的序列化方式
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        return serializer;
    }
}
```

```properties
# redis count 模式限流key过期时间, 默认1s, 例如注解写的 limit =10, 那么表示限流 qps=10
# 同理,如果limit=10, ratelimit.redis.count.expireTime = 2, 那么限流qps=5
ratelimit.redis.count.expireTime = 1
```
#### 使用注解(与Guava使用类似)
```java
@Slf4j
@RestController
public class TestRedisCountController {

    @PostMapping(value = "/ratelimit/redis/count")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.REDIS_COUNT, limit = 5, callBackMethod = "redisLimitCallBack")
    public String test1(@RequestBody Student student, @RequestParam String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        return "index";
    }

    /**
     * 限流回调方法
     */
    public void redisLimitCallBack(Student student, String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        log.error("callBack1 execute");
    }
}
```
#### 动态策略使用同Guava模式

### 分布式限流——Redis 令牌桶模式
令牌桶原理:
令牌桶是漏桶算法的改进版，漏桶是匀速处理请求，无法应对瞬时并发。
而令牌桶是创建一个固定大小的桶(这里大小就用limit), 然后均匀的速度往里面放令牌，能应对瞬时并发。
注意:
1.可用令牌永远不会超过桶大小
2.这里的放令牌时间间隔最小单位为ms毫秒, 即1秒的时间最多放1000块令牌
#### 配置Redis (RedisTemplate)
```java
略,参考上述
```
#### 配置参数
```properties
# redis bucket 模式限流key过期时间, 默认1000毫秒, 表示QPS
# 例如注解中参数 limit=10, 默认就表示限流 qps=10
# 同理,ratelimit.redis.bucket.limitTime = 2000, 注解中 limit =10 ,那么限流qps=5
ratelimit.redis.bucket.limitTime = 1000
```
#### 使用注解(与Guava使用类似)
```java
  @PostMapping(value = "/ratelimit/redis/bucket")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.REDIS_BUCKET, limit = 3, callBackMethod = "redisLimitCallBack")
    public String test1(@RequestBody Student student, @RequestParam String id) {
        return "index";
    }
```
#### 动态策略使用同Guava模式