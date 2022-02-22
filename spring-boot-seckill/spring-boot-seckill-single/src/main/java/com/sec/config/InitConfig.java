package com.sec.config;


import com.sec.pojo.entity.GoodsSeckill;
import com.sec.redis.GoodsKey;
import com.sec.service.impl.GoodsSeckillService;
import com.sec.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Configuration
@DependsOn("springContextHolder")
public class InitConfig {

    // 本地标记售罄的秒杀商品
    private HashMap<Long, Boolean> hasGoodsOverMap = new HashMap<>();

    @Autowired
    private GoodsSeckillService goodsSeckillService;

    @PostConstruct
    public void init() {
        List<GoodsSeckill> goodsSeckills = goodsSeckillService.selectAll();

        if (CollectionUtils.isEmpty(goodsSeckills)) {
            return;
        }
        // TODO: 分布式系统如何缓存?
        for (GoodsSeckill goodsSeckill : goodsSeckills) {
            // 缓存秒杀商品库存
            RedisUtil.set(GoodsKey.GOOD_STOCK, "" + goodsSeckill.getId(), String.valueOf(goodsSeckill.getStockCount()));
            // 初始化秒杀商品:未售罄
            hasGoodsOverMap.put(goodsSeckill.getId(), false);
        }
    }

    public HashMap<Long, Boolean> getHasGoodsOverMap() {
        return hasGoodsOverMap;
    }
}
