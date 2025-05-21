package com.shardinginline.controller;


import com.alibaba.fastjson.JSON;
import com.shardinginline.entity.NotSharding;
import com.shardinginline.entity.Student;
import com.shardinginline.service.NotShardingService;
import com.shardinginline.utils.StudentUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;


@Slf4j
@RestController
public class Controller_Not_In_Sharding {

    @Autowired
    private NotShardingService notShardingService;


    /**
     * 未使用库分片/表分片,用默认库 default-data-source-name: db0
     */
    @GetMapping(value = "/sharding/notsharding/insert")
    public String test1() {
        NotSharding notSharding = new NotSharding();
        int nextInt = new Random().nextInt(10000);
        notSharding.setId(nextInt);
        notSharding.setName("zhang" + nextInt);
        notSharding.setAge(nextInt);
        notShardingService.getBaseMapper().insert(notSharding);
        return JSON.toJSONString(notSharding);
    }

}
