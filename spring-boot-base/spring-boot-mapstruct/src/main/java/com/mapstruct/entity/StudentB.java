package com.mapstruct.entity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Description
 * @date 2020-03-13 15:52
 * @modify
 */

@Data
public class StudentB {
    private String name;
    private String version;
    private int age;
    private String formatDate;
    private boolean formatFlag;

    /**
     * 网点简称
     */
    private String dNbr;

}
