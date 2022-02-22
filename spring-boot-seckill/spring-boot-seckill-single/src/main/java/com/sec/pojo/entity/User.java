package com.sec.pojo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * MD5(MD5(pass明文+固定salt)+salt
     */
    private String password;

    /**
     * 混淆盐
     */
    private String salt;

    /**
     * 头像，云存储的ID
     */
    private String head;

    /**
     * 注册时间
     */
    private LocalDateTime registerDate;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;

    private static final long serialVersionUID = 1L;

    public static UserBuilder builder() {
        return new UserBuilder();
    }
}