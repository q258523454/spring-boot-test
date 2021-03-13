package com.provider.service.impl;

import com.share.service.IDubboPrintService;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: zhangj
 * @Date: 2019-09-23
 * @Version 1.0
 */

// 默认 bean("dubboPrintService")
@Service
public class DubboPrintService implements IDubboPrintService {
    private static final Logger logger = LoggerFactory.getLogger(DubboPrintService.class);

    @Override
    public String print(String str) {
        logger.warn(str);
        return "com.multi.service.dubbo.provider.DubboPrintService";
    }
}
