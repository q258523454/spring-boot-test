package com.datasource.util;

/**
 * 动态数据源的上下文 threadlocal
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> local = new ThreadLocal<>();

    private DataSourceContextHolder() {
        // Do nothing
    }

    public static void putDataSource(String name) {
        local.set(name);
    }

    public static String getCurrentDataSource() {
        return local.get();
    }

    public static void removeCurrentDataSource() {
        local.remove();
    }

}