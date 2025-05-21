package com.myjetcache.controller;

import com.myjetcache.entity.Student;
import com.myjetcache.service.RefreshService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@Slf4j
public class RefreshController {

    private final Random random = new Random();

    @Autowired
    private RefreshService refreshService;

    @GetMapping("/refreshstringcache/get")
    public String getStringCache() {
        String stringCache = refreshService.getRefreshStringCache("myStringCache");
        log.info(stringCache);
        return stringCache;
    }


    @GetMapping("/getrefreshmapcache/get")
    public Student getRefreshMapCache() {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", "1");
        body.put("id", "123");
        return refreshService.getRefreshMapCache(body);
    }
}
