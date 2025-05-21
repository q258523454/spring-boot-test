package com.consumer.service.impl;

import com.share.service.IDubboPrintService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@org.springframework.stereotype.Service
public class DubboPrintConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DubboPrintConsumer.class);

    // project1 与provider中的group名保持一致
    // @Reference(group = "project1", version = "1.0.0",url = "127.0.0.1:2181")
    // com.consumer 相当于xml中的 reference id
    // 如果不走直连,去掉 url = "dubbo://localhost:28080"
    @Reference(url = "dubbo://localhost:28080",group = "project-dubbo-provider", version = "1.0.0", registry = {"consumer1", "consumer2", "consumer3"})
    private IDubboPrintService printService1;

    public IDubboPrintService getPrintService1() {
        return printService1;
    }

}
