package redisson.controller;

import lombok.extern.slf4j.Slf4j;
import redisson.service.RedissonJackSonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RedisController {

    @Autowired
    private RedissonJackSonService redissonService;

    @PostMapping(value = "/redisson/set")
    public String set(@RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("expire") long expire) {
        redissonService.set(key, value, expire);
        return "ok";
    }

    @GetMapping(value = "/redisson/get")
    public String get(@RequestParam("key") String key) {
        return "" + redissonService.get(key);
    }

    @PostMapping(value = "/redisson/getandset")
    public String getAndSet(@RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("expire") long expire) {
        Object olderValue = redissonService.getAndSet(key, value, expire);
        log.info("older value:" + olderValue);
        log.info("new value:" + value);
        return (String) olderValue;
    }
}