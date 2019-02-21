package com.redis.entity;


import javax.validation.constraints.Min;
import java.util.Date;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-28
 */

public class Student {

    private Integer id;

    private String name;

    @Min(value = 12, message = "年龄太小,无法上学")
    private String age;

    private Date insertTime;

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
