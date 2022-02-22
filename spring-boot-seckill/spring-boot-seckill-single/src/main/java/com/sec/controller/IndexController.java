package com.sec.controller;

import com.alibaba.fastjson.JSON;
import com.sec.pojo.entity.Order;
import com.sec.service.impl.OrderService;
import com.sec.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Slf4j
@RestController
public class IndexController {


    @Autowired
    private OrderService orderService;


    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/skorder/select")
    public String select(String id) {
        Order order = orderService.selectByPrimaryKey(Long.parseLong(id));
        return JSON.toJSONString(order);
    }

    /**
     * redis 新增
     */
    @PostMapping(value = "/set")
    public String insertStudentObject(@RequestParam("key") String key, String value) {
        RedisUtil.set(key, value);
        return "finish redisUtil set task";
    }

    /**
     * redis 查询
     */
    @PostMapping(value = "/get")
    public String insertStudentObject(@RequestParam("key") String key) {
        return RedisUtil.get(key);
    }
}
