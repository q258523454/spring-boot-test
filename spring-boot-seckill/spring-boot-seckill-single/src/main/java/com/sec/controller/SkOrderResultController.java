package com.sec.controller;

import com.sec.pojo.entity.User;
import com.sec.service.impl.GoodsSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Slf4j
@RestController
public class SkOrderResultController {
    @Autowired
    private GoodsSeckillService goodsSeckillService;


    /**
     * 查询秒杀结果
     */
    @GetMapping(value = "/seckill/result")
    public String select(User user, long skGoodsId) {
        return goodsSeckillService.getSeckillResult(user, skGoodsId);
    }


}
