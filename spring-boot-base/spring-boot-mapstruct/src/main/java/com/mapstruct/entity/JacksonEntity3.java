package com.mapstruct.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;


/**
 * 作用:
 * Json序列化默认是通过 getXxx/setXxx 来检测的, 且属性名字跟 Xxx 一致, 即使字段名不是 Xxx
 * fieldVisibility = JsonAutoDetect.Visibility.ANY 让 privite属性没有getter和setter 也可以序列化
 * getter/setterVisibility= JsonAutoDetect.Visibility.NONE 让 set/get 方法不再被 jackson 检测 , 只按照原字段名序列化
 *
 * 示例: RunJsonAutoDetectTest
 * 注意：Summer框架使用, 但不使用与 FastJson
 *
 */

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class JacksonEntity3 {
    public int id;
    public String name;
    public int age;
    public String xBusCod; // 400命名

    /**    @Data默认为(Jackjson按照下面的方式格式输出), 连续的首字母XBusCod会转换成小写xbusCod
     *     但是加了上述 @JsonAutoDetect 注解,导致只按照原字段名来格式输入输出 xBusCod
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
