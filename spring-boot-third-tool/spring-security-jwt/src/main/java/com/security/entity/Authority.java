package com.security.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * 权限类型，负责存储权限和角色
 */
public class Authority implements GrantedAuthority {

    private String userAuthority;

    public Authority(String authority) {
        this.userAuthority = authority;
    }

    public void setAuthority(String authority) {
        this.userAuthority = authority;
    }

    @Override
    public String getAuthority() {
        return this.userAuthority;
    }
}
