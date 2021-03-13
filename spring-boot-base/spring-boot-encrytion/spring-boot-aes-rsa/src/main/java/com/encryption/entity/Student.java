package com.encryption.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


// 注意这里一定要实现 Serializable, 否则无法序列化 User 类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {


    private int id;

    private String name;

    private Integer age;

}