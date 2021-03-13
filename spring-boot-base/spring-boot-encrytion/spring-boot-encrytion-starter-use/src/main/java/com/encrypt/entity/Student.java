package com.encrypt.entity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @date 2020-05-12 21:42
 * @modify
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    private String name;
    private String age;

}
