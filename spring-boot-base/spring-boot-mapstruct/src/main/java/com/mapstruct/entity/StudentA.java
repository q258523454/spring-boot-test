package com.mapstruct.entity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description
 * @date 2020-03-13 15:52
 * @modify
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentA {
    private String name;
    private String version;
    private int age;
    private Date date;
    private String flag; // "Y"/"N"

    /**
     * 特殊情况, 首字母些小写,第二个字母大写
     * source 和 target 中要以首字母大写的格式存在, 因为 lombok生成get/set方法会首字母大写
     * mapstruct 的识别规则不一致，所以必须指定为  ZDbkNbr
     */
    private String zDbkNbr;




}
