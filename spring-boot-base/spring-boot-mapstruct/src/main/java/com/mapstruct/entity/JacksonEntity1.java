package com.mapstruct.entity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * @Description
 * @date 2020-03-23 20:24
 * @modify
 */


public class JacksonEntity1 {
    public int id;
    public String name;
    public int age;
    public String xBusCod; // 400命名

    public int getZhangId() {
        return id;
    }

    public void setZhangId(int id) {
        this.id = id;
    }

    public String getZhangName() {
        return name;
    }

    public void setZhangName(String name) {
        this.name = name;
    }

    public int getZhangAge() {
        return age;
    }

    public void setZhangAge(int age) {
        this.age = age;
    }

    public String getxBusCod() {
        return xBusCod;
    }

    public void setxBusCod(String xBusCod) {
        this.xBusCod = xBusCod;
    }
}
