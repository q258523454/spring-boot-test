package com.oracle.vs.jdbc.util;

import cn.hutool.core.lang.Snowflake;

public enum SnowflakeUse {
    ;
    public static final Snowflake SNOW_FLAKE = new Snowflake();

    public static long next() {
        return SNOW_FLAKE.nextId();
    }

}
