package com.myjetcache.controller;

import com.alicp.jetcache.Cache;
import com.myjetcache.service.CreateCacheUseAnnotationService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class CreateCacheUseAnnotationController {
    @Autowired
    private CreateCacheUseAnnotationService createCacheUseAnnotationService;

    @GetMapping("/createcache/annotation/set")
    public String setCreateCache() {
        Cache<String, String> stringCache = createCacheUseAnnotationService.getStringCache();
        stringCache.put("useAnnotation1", UUID.randomUUID().toString());
        stringCache.put("useAnnotation2", UUID.randomUUID().toString());
        log.info("useAnnotation1 in cache:" + stringCache.get("useAnnotation1"));
        log.info("useAnnotation2 in cache:" + stringCache.get("useAnnotation2"));
        return "ok";
    }

    @GetMapping("/createcache/annotation/get")
    public String createCache() {
        Cache<String, String> stringCache = createCacheUseAnnotationService.getStringCache();
        log.info("useAnnotation1:" + stringCache.get("useAnnotation1"));
        log.info("useAnnotation2:" + stringCache.get("useAnnotation2"));
        return "ok";
    }
}
