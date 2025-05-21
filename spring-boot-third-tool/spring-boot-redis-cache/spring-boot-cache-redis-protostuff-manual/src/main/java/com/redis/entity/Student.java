package com.redis.entity;


import lombok.Data;

/**
 * Created By
 *
 * @date :   2018-08-28
 */


@Data
public class Student {

    /**
     * 当对象只包含基本数据类型的时候,RADM工具可以直接展示数据. 非基本数据类型的字段, 默认不会展示.
     */
    private Integer id;

    private String name;

    private Integer age;
}