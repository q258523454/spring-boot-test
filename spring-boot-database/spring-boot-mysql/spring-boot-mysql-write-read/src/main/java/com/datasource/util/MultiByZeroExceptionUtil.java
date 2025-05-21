package com.datasource.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultiByZeroExceptionUtil {
    private static final Logger log = LoggerFactory.getLogger(MultiByZeroExceptionUtil.class);

    private MultiByZeroExceptionUtil() {
        // Do nothing
    }

    public static void multiByZero(int zero) {
        int b = 1 / zero;
        log.info("info:{}", b);
    }
}
