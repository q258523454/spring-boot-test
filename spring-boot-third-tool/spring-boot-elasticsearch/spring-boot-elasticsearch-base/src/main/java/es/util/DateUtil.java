package es.util;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Date: 2019-08-13
 * @Version 1.0
 */
public enum DateUtil {
    ;

    /**
     * 格式 yyyyMMdd
     */
    private static final DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 格式 yyyyMMddHHmmss
     */
    private static final DateTimeFormatter ymdHms = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 格式 yyyyMMddHHmmss
     */
    private static final DateTimeFormatter y_m_d_H_m_s = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 格式 yyyyMMddHHmmssSSS
     */
    public static final DateTimeFormatter ymdHmsSSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * 格式 MMddHHmmssSSS
     */
    private static final DateTimeFormatter mdHmsSSS = DateTimeFormatter.ofPattern("MMddHHmmssSSS");


    /**
     * 得到今天的年月日 yyyyMMdd
     */
    public static String getYmd() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(ymd);
    }

    /**
     * 得到今天的年月日-时分秒 yyyyMMddHHmmss
     */
    public static String getYmdHms() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(ymdHms);
    }

    /**
     * 得到今天的年月日-时分秒 yyyy-MM-dd HH:mm:ss
     */
    public static String getY_m_d_H_m_s() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(y_m_d_H_m_s);
    }

    /**
     * 得到今天的年月日-时分秒-毫秒 yyyyMMddHHmmssSSS
     */
    public static String getYmdHmsSSS() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(ymdHmsSSS);
    }

    /**
     * 得到今天的月日-时分秒-毫秒 MMddHHmmssSSS
     */
    public static String getmdHmsSSS() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        return localDateTime.format(mdHmsSSS);
    }

    /**
     * 得到当前 Date
     */
    public static Date getDateNow() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    /**
     * yyyyMMdd(String) 转换成 Date
     * @param yyyyMMdd
     * @return
     */
    public static Date ymdToDate(String yyyyMMdd) {
        LocalDate localDate = LocalDate.parse(yyyyMMdd, ymd);
        LocalDateTime combine = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        long millis = localDateTimeToLong(combine);
        Date date = new Date(millis);
        return date;
    }

    /**
     * yyyyMMddHHmmss(String) 转换成 Date
     * @param yyyyMMddHHmmss
     * @return
     */
    public static Date ymdHmsToDate(String yyyyMMddHHmmss) {
        LocalDateTime localDateTime = LocalDateTime.parse(yyyyMMddHHmmss, ymdHms);
        long millis = localDateTimeToLong(localDateTime);
        Date date = new Date(millis);
        return date;
    }

    /**
     * Date 类型转换成 yyyyMMdd
     */
    public static String dateToYmd(Date date) {
        long mills = date.getTime();
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), shanghai);
        return localDateTime.format(ymd);
    }

    /**
     * Date类型转换成 yyyyMMddHHmmss
     */
    public static String dateToYmdHms(Date date) {
        long mills = date.getTime();
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), shanghai);
        return localDateTime.format(ymdHms);
    }


    /**
     * long(Date) 转 LocalDateTime
     * @param mills 例如: Calendar.getInstance().getTimeInMillis()
     * @param mills 例如: Date().getTime().getTime
     * @return
     */
    public static LocalDateTime longToLocalDateTime(long mills) {
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(mills).atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * LocalDateTime 转 long (Date)
     * @param localDateTime
     * @return
     */
    public static long localDateTimeToLong(LocalDateTime localDateTime) {
        // LocalDateTime --> 时区zonedDateTime --> Instant
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
        long longTime = zonedDateTime.toInstant().toEpochMilli();
        return longTime;
    }

    /**
     * 前端传的开始日期 yyyy-MM-dd , 给数据库查的话是 当天最大的时间 yyyy-MM-dd 00:00:00:000
     */
    public static Date adjustStartDateTime(Date dateTime) {
        if (null != dateTime) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(dateTime);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);// 000
            return calendar.getTime();
        }
        return null;
    }

    /**
     * 前端传的结束日期 yyyy-MM-dd , 给数据库查的话是 当天最大的时间 yyyy-MM-dd 23:59:59:999
     */
    public static Date adjustEndDateTime(Date dateTime) {
        if (null != dateTime) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(dateTime);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * @Description: 计算日期 yyyyMMdd 之间的日期天数差值, bigDecimal1-bigDecimal2,注意会出现负值
     * @param pBigDecimal1
     * @param pBigDecimal2
     * @throws
     */
    public static long bigDecimalBetween(BigDecimal pBigDecimal1, BigDecimal pBigDecimal2) {
        LocalDate localDate1 = LocalDate.parse(pBigDecimal1.toString(), ymd);
        LocalDate localDate2 = LocalDate.parse(pBigDecimal2.toString(), ymd);
        Duration between = Duration.between(localDate1.atStartOfDay(), localDate2.atStartOfDay());
        long temp = between.toDays();
        return -temp;
    }


    public static void main(String[] args) {

        System.out.println(DateUtil.getYmd());
        System.out.println(DateUtil.getYmdHms());
        System.out.println(DateUtil.getYmdHmsSSS());
        System.out.println(DateUtil.getmdHmsSSS());
        System.out.println(DateUtil.adjustStartDateTime(new Date()));
        System.out.println(DateUtil.adjustEndDateTime(new Date()));
        System.out.println(DateUtil.longToLocalDateTime(System.currentTimeMillis()));
        System.out.println(DateUtil.longToLocalDateTime(Calendar.getInstance().getTimeInMillis()));

        BigDecimal bigDecimal1 = new BigDecimal("20200520");
        BigDecimal bigDecimal2 = new BigDecimal("20200331");
        System.out.println(DateUtil.bigDecimalBetween(bigDecimal1, bigDecimal2));
        System.out.println(DateUtil.ymdToDate("20200103"));
        System.out.println(DateUtil.ymdHmsToDate("20100101162211"));

        System.exit(0);
    }
}
