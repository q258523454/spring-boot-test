package com.myjetcache.controller;

import com.alicp.jetcache.Cache;
import com.myjetcache.service.CreateCacheUseMethodService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class CreateCacheUseMethodController {
    @Autowired
    private CreateCacheUseMethodService createCacheUseMethodService;


    @GetMapping("/createcache/method/set")
    public String setCreateCache() {
        Cache<String, String> stringCache = createCacheUseMethodService.getStringMethodCache();
        stringCache.put("useMethod1", UUID.randomUUID().toString());
        stringCache.put("useMethod2", UUID.randomUUID().toString());
        log.info("useMethod1 in cache:" + stringCache.get("useMethod1"));
        log.info("useMethod2 in cache:" + stringCache.get("useMethod2"));
        return "ok";
    }

    @GetMapping("/createcache/method/set/null")
    public String setCreateCacheNull() {
        Cache<String, String> stringCache = createCacheUseMethodService.getStringMethodCache();
        stringCache.put("useMethod1", null);
        stringCache.put("useMethod2", null);
        log.info("useMethod1 in cache:" + stringCache.get("useMethod1"));
        log.info("useMethod2 in cache:" + stringCache.get("useMethod2"));
        return "ok";
    }


    @GetMapping("/createcache/method/get")
    public String createCache() {
        Cache<String, String> stringCache = createCacheUseMethodService.getStringMethodCache();
        log.info("useMethod1:" + stringCache.get("useMethod1"));
        log.info("useMethod2:" + stringCache.get("useMethod2"));
        return "ok";
    }
}
