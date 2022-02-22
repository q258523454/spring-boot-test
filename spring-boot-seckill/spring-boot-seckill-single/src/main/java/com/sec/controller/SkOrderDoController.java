package com.sec.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.sec.config.InitConfig;
import com.sec.pojo.dto.SeckillMessage;
import com.sec.pojo.entity.GoodsSeckill;
import com.sec.rabbitmq.producer.DirectSender;
import com.sec.redis.GoodsKey;
import com.sec.redis.SeckillOrderKey;
import com.sec.service.impl.GoodsSeckillService;
import com.sec.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Slf4j
@RestController
public class SkOrderDoController {

    @Autowired
    private GoodsSeckillService goodsSeckillService;

    @Autowired
    private InitConfig initConfig;

    @Autowired
    private DirectSender directSender;

    /**
     * 基于令牌桶算法的限流实现类,每秒发50个令牌
     */
    private RateLimiter rateLimiter = RateLimiter.create(50);

    /**
     * 秒杀
     */
    @GetMapping(value = "/seckill/do_seckill")
    public String doSeckill(Long userId, long skGoodsId) {
        if (null == userId) {
            log.info("userId is null");
            return "userId is null";
        }
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            log.info("can not been granted");
            return "can not been granted";
        }

        boolean over = false;

        // 本地缓存-判断是否售罄, TODO: 本地缓存不适用于分布式系统, 如何改造?
        over = initConfig.getHasGoodsOverMap().get(skGoodsId);
        if (over) {
            return "userId:" + userId + ",goods:" + skGoodsId + " is over.";
        }

        // 判断重复秒杀(根据用户id+商品id, 一个用户对同一个商品至多秒杀一次）
        String res = RedisUtil.get(SeckillOrderKey.USER_ORDER, userId + ":" + skGoodsId);
        if (!StringUtils.isEmpty(res)) {
            log.info("repeat seckill.");
            return "repeat seckill.";
        }

        // Redis INCR 操作是原子性的,线程安全.
        long stock = RedisUtil.incr(GoodsKey.GOOD_STOCK, "" + skGoodsId, -1);
        // 高并发时候,可能多个请求同时进入下面这段if
        // TODO: 这里可以用 redis+lua 来写, 直接判断是否库存足够扣减
        if (stock < 0) {
            // 高并发下避免执行所有进入这段if的请求, 先判断本地缓存-判断是否售罄
            // TODO: 本地缓存不适用于分布式系统, 如何改造?
            over = initConfig.getHasGoodsOverMap().get(skGoodsId);
            if (over) {
                return "goods:" + skGoodsId + " is over.";
            }
            GoodsSeckill goodsSeckill = goodsSeckillService.selectById(skGoodsId);
            if (goodsSeckill.getStockCount() <= 0) {
                // 本地缓存标记为售罄, 暂不考虑 未支付 或 取消订单
                initConfig.getHasGoodsOverMap().put(skGoodsId, true);
                return "goods:" + skGoodsId + " is over.";
            }
        }

        // 发送MQ
        SeckillMessage message = new SeckillMessage();
        message.setUserId(userId);
        message.setSkGoodsId(skGoodsId);
        directSender.send(message);

        log.info(userId + " wait result");
        return userId + " wait result";
    }
}
