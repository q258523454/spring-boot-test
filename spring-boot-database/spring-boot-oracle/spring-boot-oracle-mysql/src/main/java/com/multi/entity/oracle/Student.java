package com.multi.entity.oracle;

import java.math.BigDecimal;

public class Student {
    // id
    private BigDecimal id;

    // 姓名
    private String name;

    // 年龄id
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

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }
}