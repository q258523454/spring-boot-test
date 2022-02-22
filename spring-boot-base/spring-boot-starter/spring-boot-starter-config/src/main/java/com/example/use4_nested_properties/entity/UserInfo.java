package com.example.use4_nested_properties.entity;


import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
public class UserInfo {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户年龄
     */
    private String age;

    /**
     * 住址信息, 再次嵌套
     */
    @NestedConfigurationProperty
    private AddressInfo address;
}
