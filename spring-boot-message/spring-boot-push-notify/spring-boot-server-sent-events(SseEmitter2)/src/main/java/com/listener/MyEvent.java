package com.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: zhangj
 * @Date: 2019-11-28
 * @Version 1.0
 */
public class MyEvent extends ApplicationEvent {
    private static final Logger logger = LoggerFactory.getLogger(MyEvent.class);

    private String id;

    private String msg;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */

    public MyEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public MyEvent(Object source, String id, String msg) {
        super(source);
        this.id = id;
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
