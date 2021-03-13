package com.lombok.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentCallSupper extends Identity {

    private String name;

    private int age;

}
