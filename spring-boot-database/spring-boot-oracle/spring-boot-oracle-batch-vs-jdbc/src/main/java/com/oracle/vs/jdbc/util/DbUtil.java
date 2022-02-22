package com.oracle.vs.jdbc.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Slf4j
public class DbUtil {
    /**
     * 关闭PreparedStatement资源
     */
    public static void close(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (Exception e) {
            log.error("关闭PreparedStatement资源异常", e);
            preparedStatement = null;
        }
    }

    /**
     * 关闭connection资源
     */
    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.error("关闭Connection资源异常", e);

            connection = null;
        }
    }

    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error("记录rollback时发生异常", ex);
            }
        }
    }
}
