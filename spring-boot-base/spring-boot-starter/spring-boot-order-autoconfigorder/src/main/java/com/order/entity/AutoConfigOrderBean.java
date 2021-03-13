package com.order.entity;

/**
 * @Description
 * @date 2020-04-02 10:32
 * @modify
 */
public class AutoConfigOrderBean {

    public static String name = "未初始化 ";

    public static void setName(String beanName) {
        name = beanName + " 完成初始化";
    }

}
