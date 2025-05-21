package com.multidb.anno;

public class DataSourceHolder {

    /**
     * The constant HOLDER.
     */
    public static final ThreadLocal<String> DATA_NAME = new ThreadLocal<>();

    /**
     * Gets data source.
     *
     * @return the data source
     */
    public static String getDataSource() {
        return DATA_NAME.get();
    }

    /**
     * Sets data source.
     *
     * @param dataSourceName the data source name
     */
    public static void setDataSource(String dataSourceName) {
        DATA_NAME.set(dataSourceName);
    }

    /**
     * Remove data source.
     */
    public static void removeDataSource() {
        DATA_NAME.remove();
    }

}
