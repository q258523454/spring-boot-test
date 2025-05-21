package redisson.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping(value = "/redisson")
    public String test() {
        RBucket<Object> bucket = redissonClient.getBucket("zhangsan");
        bucket.set("18");
        System.out.println(bucket.get());
        return "OK";
    }

}