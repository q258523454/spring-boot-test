package com.trans.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Long id;
    private String username;
    private String password;
    private Date regTime;

    public Student(String username, String password, Date regTime) {
        this.username = username;
        this.password = password;
        this.regTime = regTime;
    }

    public static Student createStudent(String name) {
        String username = name;
        if (StringUtils.isEmpty(name)) {
            username = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 3);
        }
        String passwd = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
        return new Student(username, passwd, new Date());
    }
}
