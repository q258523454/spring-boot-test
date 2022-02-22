package com.oracle.vs.jdbc.service.impl;

import com.oracle.vs.jdbc.config.ConnectPoolConfig;
import com.oracle.vs.jdbc.config.DataSourceConfig;
import com.oracle.vs.jdbc.service.ConnectPoolService;
import com.oracle.vs.jdbc.util.DbUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConnectPoolServiceImpl implements ConnectPoolService {


    private final static String ORACLE_DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
    /**
     * 数据库信息
     */
    private DataSourceConfig dataSourceConfig;
    /**
     * 用户信息
     */
    private Properties properties;

    /**
     * 连接池配置
     */
    private ConnectPoolConfig connectPoolConfig;
    /**
     * 连接池是否活动
     */
    private boolean isActive;

    /**
     * 空闲数据库连接的列表
     */
    private BlockingQueue<Connection> idleConnections;
    /**
     * 所有的数据库连接列表
     */
    private List<Connection> allConnections;
    /**
     * 正在创建的连接个数
     */
    private AtomicInteger createCount = new AtomicInteger(0);


    public ConnectPoolServiceImpl(DataSourceConfig dataSourceInfo, ConnectPoolConfig config) {
        this.dataSourceConfig = dataSourceInfo;
        init();
        this.connectPoolConfig = config;
        this.idleConnections = new ArrayBlockingQueue<>(config.getMaxSize());
        this.allConnections = new ArrayList<>(config.getMaxSize());
        isActive = true;
    }


    private void init() {
        try {
            // 装载数据库驱动
            Class.forName(ORACLE_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            String msg = MessageFormat.format("数据库驱动类加载失败，驱动类名：{0}，dataSourceInfo：{1}", ORACLE_DRIVER_NAME, dataSourceConfig);
            throw new RuntimeException(msg, e);
        }
        properties = new Properties();
        properties.setProperty("user", dataSourceConfig.getUser());
        properties.setProperty("password", dataSourceConfig.getPassword());
    }

    @Override
    public Connection getConnection() {
        if (!isActive) {
            String msg = MessageFormat.format("连接池已关闭，无法获取连接，dataSourceInfo：{0}", dataSourceConfig);
            throw new RuntimeException(msg);
        }

        // 首先从空闲队列获得连接, poll: 有则删除并返回该值，空则返回 null
        Connection conn = this.idleConnections.poll();
        if (conn == null) {
            int currentConn = allConnections.size() + createCount.intValue();
            // 当前连接数已经超过最大值,不再创建 connection
            if (currentConn > connectPoolConfig.getMaxSize()) {
                log.info("连接池已满，总连接数为：{}", currentConn);
                try {
                    conn = this.idleConnections.poll(connectPoolConfig.getWaitIdleConnSeconds(), TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    // TODO
                }
                if (conn == null) {
                    throw new RuntimeException(MessageFormat.format("连接池已满，且等待{0}秒仍然获取不到连接", connectPoolConfig.getWaitIdleConnSeconds()));
                }
            } else {
                // 连接数没有达到最大值,直接创建 connection
                conn = createNewConnection();
            }
        }
        log.info("线程({})获取连接成功，当前总连接数{}空闲连接数{}", Thread.currentThread().getName(), allConnections.size() + createCount.intValue(), idleConnections.size());
        return conn;
    }

    /**
     * 归还连接，不检查是否提交了事务，需要自行确保
     */
    @Override
    public void returnConnection(Connection conn) {
        // 如果连接池已经关闭了,直接断开connection
        if (!isActive) {
            DbUtil.close(conn);
        } else {
            // 如果队列没满,插入返回 true. 队列满了会插入失败,返回 false.
            if (!idleConnections.offer(conn)) {
                DbUtil.close(conn);
                allConnections.remove(conn);
            }
        }
        log.info("线程({})归还连接，当前总连接数{}空闲连接数{}", Thread.currentThread().getName(), allConnections.size() + createCount.intValue(), idleConnections.size());
    }

    /**
     * 关闭连接池，不判断是否有正在使用的连接，需自行确保
     */
    @Override
    public void destroy() {
        if (!isActive) {
            return;
        }
        for (Connection conn : allConnections) {
            DbUtil.close(conn);
        }
        isActive = false;
        log.info("连接池关闭成功");
    }

    /**
     * 建立数据库连接
     */
    private Connection createNewConnection() {
        log.info("数据库连接池暂无空闲连接(总连接数:{})，新建连接...", createCount.intValue() + allConnections.size());
        createCount.incrementAndGet();
        //获取连接
        try {
            Connection conn = DriverManager.getConnection(dataSourceConfig.getUrl(), properties);
            allConnections.add(conn);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(MessageFormat.format("创建数据库连接异常，dataSourceInfo：{0}", dataSourceConfig));
        } finally {
            createCount.decrementAndGet();
        }
    }


}
