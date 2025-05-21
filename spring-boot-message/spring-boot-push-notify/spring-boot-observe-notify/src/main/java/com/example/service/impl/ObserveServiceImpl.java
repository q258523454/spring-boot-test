package com.example.service.impl;

import com.example.service.ObserveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Observable;



// 被观察的服务(通知类)
@Service("observeService")
public class ObserveServiceImpl extends Observable implements ObserveService {
    private static final Logger logger = LoggerFactory.getLogger(ObserveServiceImpl.class);

    @Override
    public String doBussiness(String msg) {
        this.setChanged();
        this.notifyObservers(msg);
        return "Finished message send";
    }

}
