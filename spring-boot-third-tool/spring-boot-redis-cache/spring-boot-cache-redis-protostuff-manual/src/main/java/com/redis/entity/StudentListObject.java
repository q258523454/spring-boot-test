
package com.redis.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class StudentListObject {

    /**
     * RADM 工具只能展示这个字段的数据
     */
    private String name;

    /**
     * 非基本数据类型 RADM 工具默认不展示
     */
    private List<Student> studentList = new ArrayList<>();
}
