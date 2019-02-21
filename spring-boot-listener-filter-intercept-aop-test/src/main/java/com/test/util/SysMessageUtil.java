package com.test.util;

import com.test.entity.SysMessage;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-29
 */
public class SysMessageUtil {

    public static SysMessage error(Integer code, String msg) {
        SysMessage message = new SysMessage();
        message.setCode(code);
        message.setMsg(msg);
        message.setStatus("error");
        return message;
    }

    public static SysMessage error(Integer code, String msg, String url) {
        SysMessage message = new SysMessage();
        message.setCode(code);
        message.setMsg(msg);
        message.setStatus("error");
        message.setUrl(url);
        return message;
    }

    public static SysMessage success(Integer code, String msg, Object object) {
        SysMessage message = new SysMessage();
        message.setMsg(msg);
        message.setCode(code);
        message.setData(object);
        message.setStatus("success");
        return message;
    }


}
