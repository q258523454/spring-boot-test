package com.oracle.vs.jdbc;

import com.oracle.vs.jdbc.util.SnowflakeUse;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class T0_Run1_Oracle_JDBC_Insert_One_Org implements Runnable {

    @Override
    public void run() {
        String url = "jdbc:oracle:thin:@//localhost:1521/EA0DB100";
        String name = "oracle.jdbc.driver.OracleDriver";
        String user = "TEST";
        String password = "XXX#123456";
        Connection conn = null;
        try {
            Class.forName(name);
            // 获取连接
            conn = DriverManager.getConnection(url, user, password);
            // 关闭自动提交，不然conn.commit()运行到这句会报错
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error");
        }
        // 开始时间
        Long begin = System.currentTimeMillis();
        // sql前缀
        String sqlTemplate = "INSERT INTO TEST.STUDENT(ID, NAME, AGE) VALUES (?,?,?)";
        try {
            // 设置事务为非自动提交
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(sqlTemplate);
            String age = Double.toString(18 + (int) (Math.random() * ((25 - 18) + 1)));
            stmt.setBigDecimal(1, BigDecimal.valueOf(SnowflakeUse.next()));
            stmt.setString(2, "test");
            stmt.setBigDecimal(3, new BigDecimal(age));
            stmt.addBatch();
            stmt.executeBatch();
            // 提交事务, 先关闭自动提交conn.setAutoCommit(false);
            conn.commit();
            // 关闭资源
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 结束时间
        Long end = System.currentTimeMillis();
        // 耗时
        System.out.println("线程" + Thread.currentThread().getName() + "数据插入耗时: " + (end - begin) + " ms");
    }


    public static void main(String[] args) {
        T0_Run1_Oracle_JDBC_Insert_One_Org myRunnable = new T0_Run1_Oracle_JDBC_Insert_One_Org();
        Thread thread = new Thread(myRunnable); // 对同一个对象创建多线程
        thread.start();
    }
}
