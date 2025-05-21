package shiro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 */
public enum TimeUtils {

    ;

    /**
     * 获取 yyyy-MM-dd HH:mm:ss 格式的当前时间
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        s.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return s.format(new Date());
    }
}
