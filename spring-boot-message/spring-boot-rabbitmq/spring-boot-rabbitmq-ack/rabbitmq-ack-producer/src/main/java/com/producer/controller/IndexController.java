package com.producer.controller;

import com.ack.common.entity.BrokerMessageLog;
import com.ack.common.entity.Order;
import com.ack.common.serivce.BrokerMessageLogService;
import com.ack.common.serivce.OrderService;
import com.alibaba.fastjson.JSON;
import com.producer.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class IndexController {

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrokerMessageLogService brokerMessageLogService;


    @GetMapping(value = "/send")
    public String send() {
        //order对象必须实现序列化
        Order order = new Order();
        order.setName(UUID.randomUUID().toString());
        order.setMessageId(UUID.randomUUID().toString() + System.currentTimeMillis());
        rabbitService.createOrder(order, "order.abc");
        return "success";
    }

    /**
     * routeKey找不到对应的queue, 不会报错, 发送者也无法感知
     */
    @GetMapping(value = "/send/blackhole")
    public String blackhole() {
        Order order = new Order();
        order.setName(UUID.randomUUID().toString());
        order.setMessageId(UUID.randomUUID().toString() + System.currentTimeMillis());
        rabbitService.createOrder(order, "zhang.abc");
        return "success";
    }

    @GetMapping(value = "/select/all")
    public String get() {
        List<BrokerMessageLog> brokerMessageLogs = brokerMessageLogService.selectAll();
        return JSON.toJSONString(brokerMessageLogs);
    }

}
