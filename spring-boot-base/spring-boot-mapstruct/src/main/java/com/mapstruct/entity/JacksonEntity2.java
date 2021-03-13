package com.mapstruct.entity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import lombok.Data;

/**
 * @Description
 * @date 2020-04-24 15:11
 * @modify
 */
@Data
public class JacksonEntity2 {
    public int id;
    public String name;
    public int age;
    private String xBusCod;  // 400命名

    /**    @Data默认为， 注意:Jackjson默认是按照 get/set 来格式输入输出的,
     *     连续的首字母XBusCod会转换成小写xbusCod
     *
     *
     *     public String getXBusCod() {
     *         return this.xBusCod;
     *     }
     *
     *     public void setXBusCod(String xBusCod) {
     *         this.xBusCod = xBusCod;
     *     }
     */

}

