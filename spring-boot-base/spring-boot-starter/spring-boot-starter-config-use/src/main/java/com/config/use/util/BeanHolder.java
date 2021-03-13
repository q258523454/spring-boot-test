package com.config.use.util;


import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @date 2020-05-20 21:40
 * @modify
 */

@Slf4j
public class BeanHolder {

    public static <T> T getBean(Class<T> requiredType) {
        T bean;
        try {
            bean = SpringContextHolder.getBean(requiredType);
        } catch (Exception ex) {
            bean = null;
        }
        return bean;
    }

}
