package com.datasource.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */
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
