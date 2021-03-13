package com.service.impl;

import com.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2019-06-12
 * @Version 1.0
 */

@Service
public class CacheServiceImpl implements CacheService {

    private final static Logger log = LoggerFactory.getLogger(CacheServiceImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    @CacheEvict(cacheNames = "tableCache", allEntries = true)
    public void refresh() {
        log.info("time:{},refresh all cache.", dateFormat.format(new Date()));
    }
}
