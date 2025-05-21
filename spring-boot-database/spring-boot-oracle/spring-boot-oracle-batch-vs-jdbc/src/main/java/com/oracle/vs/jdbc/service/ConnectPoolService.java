package com.oracle.vs.jdbc.service;


import java.sql.Connection;

public interface ConnectPoolService {
    /**
     * 获取连接
     * @return Connection
     */
    Connection getConnection();

    /**
     * 归还连接
     * @param conn Connection
     */
    void returnConnection(Connection conn);

    /**
     * 关闭连接池
     */
    void destroy();
}
