package com.privder.serivce.impl;

import com.share.service.IDubboPrintService;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



// registry指定注册中心,默认是全部
// @Service(registry = {"provider1","provider3"})
@Service(group = "project-dubbo-provider", version = "1.0.0",registry = {"provider1", "provider2", "provider3"})
@org.springframework.stereotype.Service("DP.PrintService1")
public class IDubboPrintServiceImpl implements IDubboPrintService {
    private static final Logger logger = LoggerFactory.getLogger(IDubboPrintServiceImpl.class);

    @Override
    public String print(String str) {
        logger.info("{}-{}-{}:{}", "project1", "1.0.0", "provider1 provider2 provider3", str);
        return "provider1+provider2+provider3";
    }
}
