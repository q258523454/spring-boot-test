package com.oracle.vs.jdbc;

import com.oracle.vs.jdbc.config.ConnectPoolConfig;
import com.oracle.vs.jdbc.config.DataSourceConfig;
import com.oracle.vs.jdbc.service.impl.ConnectPoolServiceImpl;
import com.oracle.vs.jdbc.util.DbUtil;
import com.oracle.vs.jdbc.util.SnowflakeUse;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T1_Run2_JDBC_Insert_One implements Runnable {


    private ConnectPoolServiceImpl oracleConnect;

    private CountDownLatch countDownLatch;


    public T1_Run2_JDBC_Insert_One(ConnectPoolServiceImpl oracleConnect, CountDownLatch countDownLatch) {
        this.oracleConnect = oracleConnect;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = oracleConnect.getConnection();
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error");
        }

        // 开始时间
        Long begin = System.currentTimeMillis();
        // sql前缀
        String sqlTemplate = "INSERT INTO TEST.STUDENT(ID, NAME, AGE) VALUES (?,?,?)";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sqlTemplate);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error");
        }

        try {
            // 设置事务为非自动提交
            connection.setAutoCommit(false);
            String age = Double.toString(18 + (int) (Math.random() * ((25 - 18) + 1)));
            stmt.setBigDecimal(1, BigDecimal.valueOf(SnowflakeUse.next()));
            stmt.setString(2, "test");
            stmt.setBigDecimal(3, new BigDecimal(age));
            stmt.addBatch();
            stmt.executeBatch();
            // 提交事务, 先关闭自动提交conn.setAutoCommit(false);
            connection.commit();

            // 在程序关闭的时候再关闭
            //connection.close();
        } catch (SQLException e) {
            DbUtil.rollback(connection);
            e.printStackTrace();
        } finally {
            // 关闭 Statement
            DbUtil.close(stmt);
            // 归还连接
            oracleConnect.returnConnection(connection);
            countDownLatch.countDown();
        }
        // 结束时间
        Long end = System.currentTimeMillis();
        System.out.println("线程" + Thread.currentThread().getName() + "数据插入耗时: " + (end - begin) + " ms");
    }


    public static void main(String[] args) throws InterruptedException {
        ConnectPoolConfig connectPoolConfig = ConnectPoolConfig.builder()
                .maxSize(10)
                .waitIdleConnSeconds(3)
                .build();

        DataSourceConfig dataSourceConfig = DataSourceConfig.builder()
                .url("jdbc:oracle:thin:@//localhost:1521/EA0DB100")
                .user("TEST")
                .password("XXX#123456")
                .build();


        ConnectPoolServiceImpl oracleConnect = new ConnectPoolServiceImpl(dataSourceConfig, connectPoolConfig);

        int threadNum = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        T1_Run2_JDBC_Insert_One myRunnable = new T1_Run2_JDBC_Insert_One(oracleConnect, countDownLatch);

        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            // 对同一个对象创建多线程
            executor.execute(myRunnable);
        }

        // 等待全部线程完成
        countDownLatch.await(60, TimeUnit.MINUTES);
        // 所有任务执行完成,关闭连接池
        oracleConnect.destroy();
        System.exit(0);

    }
}
