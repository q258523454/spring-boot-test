package com.lombok.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Identity {
    private int id;

    public Identity() {
        System.out.println("Identity()无参构造函数");
    }
}
