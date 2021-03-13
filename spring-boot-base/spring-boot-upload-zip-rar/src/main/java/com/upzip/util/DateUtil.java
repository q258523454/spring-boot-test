package com.upzip.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Date: 2019-08-13
 * @Version 1.0
 */
public enum DateUtil {
    INSTANCE;

    private final DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DateTimeFormatter ymdHms = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final DateTimeFormatter ymdHmsSSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final DateTimeFormatter mdHmsSSS = DateTimeFormatter.ofPattern("MMddHHmmssSSS");


    DateUtil() {
        // do nothing
    }

    /**
     * 得到今天的年月日 yyyyMMdd
     *
     * @return
     */
    public String getYmd() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(ymd);
    }

    public String getYmdHms() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(ymdHms);
    }

    public String getYmdHmsSSS() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(ymdHmsSSS);
    }

    public String getmdHmsSSS() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(mdHmsSSS);
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.INSTANCE.getYmd());
        System.out.println(DateUtil.INSTANCE.getYmdHms());
        System.out.println(DateUtil.INSTANCE.getYmdHmsSSS());
        System.out.println(DateUtil.INSTANCE.getmdHmsSSS());
        System.exit(0);
    }
}
