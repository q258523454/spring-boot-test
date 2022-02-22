package com.oracle.vs.jdbc.config;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectPoolConfig {

    /**
     * 最大连接数
     */
    private int maxSize = 50;

    /**
     * 等待空闲连接的时间，一般最大连接数要设置比线程数多
     */
    private int waitIdleConnSeconds = 3;
}
