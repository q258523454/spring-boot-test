package com.example.observer;

import com.example.service.AbstractObServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Observable;


@Component
public class ObServer1 extends AbstractObServer {
    private static final Logger logger = LoggerFactory.getLogger(ObServer1.class);


    @Override
    protected void subUpdate(Observable o, Object arg) {
        logger.info("ObServer1");
    }
}
