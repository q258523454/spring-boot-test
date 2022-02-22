package com.oracle.vs.jdbc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfig {

    /**
     * 数据库连接串
     */
    private String url;

    /**
     * 用户
     */
    private String user;

    /**
     * 密码
     */
    @ToString.Exclude
    private String password;

}
