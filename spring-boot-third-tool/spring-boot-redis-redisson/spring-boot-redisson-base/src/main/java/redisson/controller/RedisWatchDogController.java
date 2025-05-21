package redisson.controller;

import lombok.extern.slf4j.Slf4j;
import redisson.service.RedissonJackSonService;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class RedisWatchDogController {

    @Autowired
    private RedissonJackSonService redissonService;

    /**
     * 方式1: lock.lock();
     * 该方式获取锁后默认过期30秒, 每过三分之一(10s)的时间都再次续期.因此, 即使应用重启, 锁也会在30秒内释放
     * 方式2 lock.tryLock(waitTime,leaseTime,TimeUnit);
     * 该方式主动管理尝试加锁时间和默认释放时间
     * 注意释放锁必须检查2项：
     * ① 锁状态: isLocked 是否为 true
     * ② 线程是否持有锁: 使用 lock.isHeldByCurrentThread() 方法检查当前线程是否持有锁
     */
    @GetMapping(value = "/redisson/watchdog/normal")
    public String watchDogNormal() {
        RLock lock = redissonService.getLock("my-lock");
        boolean isLocked = false;
        try {
            // 尝试加锁，最多等待5秒，加锁后10秒释放
            isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            log.info("locked success, thread id is:{}", Thread.currentThread().getId());
            return "ok";
        } catch (InterruptedException e) {
            throw new RuntimeException("system error");
        } finally {
            // 释放时必须确保已经上锁并且当前线程仍持有锁, 否则unlock()会报错:attempt to unlock lock
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("lock unlock success");
            }
        }
    }

    /**
     * 看门狗测试:
     * 8081先请求,8082然后请求. 8082会阻塞
     * 1.如果8081不关闭应用也不主动释放锁,8082 会一直阻塞.
     * 2.如果8081关闭应用程序,则 30秒 后,8082会请求成功.
     * 3.如果8081主动释放锁, 则 8082立马请求成功.
     */
    @GetMapping(value = "/redisson/watchdog/unfree")
    public String unfree() {
        log.info(Thread.currentThread().getId() + ":尝试获取锁");
        RLock lock = redissonService.getLock("my-lock");
        lock.lock();
        log.info(Thread.currentThread().getId() + ":获取锁成功");
        return "Hello!";
    }
}