package com.encrypt.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @date 2020-05-18 15:46
 * @modify
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BodyObject implements Serializable {
    Student student;
}
