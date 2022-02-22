package com.oracle.vs.jdbc;

import com.alibaba.fastjson.JSON;
import com.oracle.vs.jdbc.config.ConnectPoolConfig;
import com.oracle.vs.jdbc.config.DataSourceConfig;
import com.oracle.vs.jdbc.service.impl.ConnectPoolServiceImpl;
import com.oracle.vs.jdbc.util.DbUtil;
import com.oracle.vs.mybatis.entity.Student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class T2_Run2_JDBC_Insert_Batch implements Runnable {

    private ConnectPoolServiceImpl oracleConnect;

    private int num;

    private Random random = new Random();


    public T2_Run2_JDBC_Insert_Batch(ConnectPoolServiceImpl oracleConnect) {
        this.oracleConnect = oracleConnect;
    }

    public T2_Run2_JDBC_Insert_Batch(ConnectPoolServiceImpl oracleConnect, int num) {
        this.oracleConnect = oracleConnect;
        this.num = num;
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

        // 查询最大id
        Long maxId = null;
        try {
            String selectSql = "SELECT MAX(id) FROM STUDENT";
            Statement selectStmt = null;
            selectStmt = connection.createStatement();
            // 查询最大id
            ResultSet resultSet = selectStmt.executeQuery(selectSql);

            while (resultSet.next()) {
                maxId = resultSet.getLong("MAX(id)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询出错");
        }

        // insert sql 前缀
        String sqlTemplate = "INSERT INTO TEST.STUDENT(ID, NAME, AGE) VALUES (?,?,?)";
        PreparedStatement insertStmt = null;
        try {
            insertStmt = connection.prepareStatement(sqlTemplate);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("创建statement失败.");
        }

        try {
            // 设置事务为非自动提交
            connection.setAutoCommit(false);

            long tempMaxId = (maxId == null) ? 0 : maxId + 1;
            for (int i = 0; i < this.num; i++) {
                long pId = tempMaxId + i;
                Student student = getStudent(pId + "");
                insertStmt.setBigDecimal(1, student.getId());
                insertStmt.setString(2, student.getName());
                insertStmt.setBigDecimal(3, student.getAge());
                insertStmt.addBatch();
            }
            /**
             * executeBatch 返回的是 int[] 数组,和批处理中的执行的SQL一一对应,值代表影响的行数。
             * 元素>=0,影响的行数。
             * 元素=-2,执行成功,但无法获取影响的行数。
             * 元素=-3,执行失败
             */
            int[] result = insertStmt.executeBatch();
            int updateCount = insertStmt.getUpdateCount();
            System.out.println("result= " + JSON.toJSONString(result));
            System.out.println("updateCount= " + updateCount);

            // 提交事务, 先关闭自动提交conn.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            DbUtil.rollback(connection);
            e.printStackTrace();
            throw new RuntimeException("SQL执行错误");
        } finally {
            // 关闭 Statement
            DbUtil.close(insertStmt);
            // 归还连接 connect
            oracleConnect.returnConnection(connection);
        }
        // 结束时间
        Long end = System.currentTimeMillis();
        System.out.println("线程" + Thread.currentThread().getName() + "数据插入耗时: " + (end - begin) + " ms");
    }


    public Student getStudent(String id) {
        Student student = new Student();
        student.setId(new BigDecimal(id));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("" + random.nextInt(9999)));
        return student;
    }

    /**
     * 测试结果:
     * JDBC batch-prepared
     * 1000条: 198,206,178 ms
     * 5000条: 248,289,227 ms
     * 10000条: 281,1297,299 ms
     * 50000条: 1132,1065,735 ms
     * 100000条: 713,758,886 ms
     */
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

        int threadNum = 1;
        int insertNum = 5;
        T2_Run2_JDBC_Insert_Batch myRunnable = new T2_Run2_JDBC_Insert_Batch(oracleConnect, insertNum);

        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        executorService.execute(myRunnable);

        // 停止接受新任务,当已有任务将执行完,关闭线程池
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // 等待所有线程执行完成
        }

        // 所有任务执行完成,关闭连接池
        oracleConnect.destroy();
        System.exit(0);
    }
}
