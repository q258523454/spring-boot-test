package com.oracle.base.entity;

import java.math.BigDecimal;

public class Student {
    /**
     * null
     */
    private BigDecimal id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 姓名2
     */
    private String name2;

    /**
     * 年龄
     */
    private BigDecimal age;


    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }
}