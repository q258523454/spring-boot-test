package com.example.springbootexcel.controller;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

/**
 * @date 2022-07-28 11:26
 */

@Slf4j
@RestController
public class Controller_Index {
    @GetMapping(value = "/index")
    public String index() {
        log.info("index33");
        return "index33";
    }

    @PostMapping(value = "/restTemplate/post")
    public String index2(
            @RequestParam(value = "id", required = false) String id,
            @RequestBody List<String> nameList) {
        log.info(id);
        log.info(JSON.toJSONString(nameList));
        return JSON.toJSONString(nameList);
    }

}
