package com.test.util;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-29
 */
public enum SysMessageEnum {
    Exception(40000, "Error: Exception"),
    NullPointerException(40001, "Error: NumberFormatException"),
    NumberFormatException(40002, "Error: NumberFormatException"),
    ;
    private Integer code;
    private String msg;
    private Exception e;

    SysMessageEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
