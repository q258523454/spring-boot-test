package com.myjetcachekryo.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

/**
 * Created By
 *
 * @date :   2018-08-28
 */


@Data
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Random RANDOM = new Random();

    /**
     * 当对象只包含基本数据类型的时候,RADM工具可以直接展示数据. 非基本数据类型的字段, 默认不会展示.
     */
    private Integer id;

    private String name;

    private Integer age;

    public static Student getStudent(String id) {
        Student student = new Student();
        student.setId(Integer.parseInt(id));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(RANDOM.nextInt(9999));
        return student;
    }
}