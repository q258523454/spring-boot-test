package com.sec.controller;

import com.alibaba.fastjson.JSON;
import com.sec.pojo.entity.Order;
import com.sec.pojo.entity.User;
import com.sec.service.impl.GoodsSeckillService;
import com.sec.service.impl.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Slf4j
@RestController
public class SkOrderSearchController {
    @Autowired
    private GoodsSeckillService goodsSeckillService;

    @Autowired
    private OrderService orderService;

    /**
     * 查询订单详情
     */
    @GetMapping(value = "/seckill/order/detail")
    public String orderDetail(User user, long skGoodsId) {
        List<Order> orders = orderService.selectByUserIdAndSkGoodsId(user.getId(), skGoodsId);
        return JSON.toJSONString(orders);
    }
}
