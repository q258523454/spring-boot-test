package com.example.observer;

import com.example.service.AbstractObServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;


@Component
public class ObServer2 extends AbstractObServer {
    private static final Logger logger = LoggerFactory.getLogger(ObServer2.class);

    @Override
    public void subUpdate(Observable o, Object arg) {
        logger.info("ObServer2");
    }


}
