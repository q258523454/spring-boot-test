package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

/**
 * @Author: zhangj
 * @Date: 2019-09-24
 * @Version 1.0
 */
public abstract class AbstractObServer implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractObServer.class);

    // 注入通知服务
    @Autowired(required = true)
    @Qualifier(value = "observeService")
    public Observable observable;

    @PostConstruct
    public void init() {
        // 向通知服务加入观察者(每个继承自AbstractObServer的类)
        observable.addObserver(this);
        logger.info("finished add observer:{}", this.getClass().getName());
    }


    @Override
    public void update(Observable o, Object arg) {
        // TODO 可以进行抽象层的参数检查
        String msg = "";
        if (arg instanceof String) {
            logger.info("通知信息是String类型");
            msg = (String) arg;
        } else {
            logger.info("通知信息不是String类型");
        }
        logger.info("{} get messages {}", this.getClass().getName(), msg);
        subUpdate(o, arg);
    }

    // 各个观察者自己的业务逻辑
    protected abstract void subUpdate(Observable o, Object arg);
}
