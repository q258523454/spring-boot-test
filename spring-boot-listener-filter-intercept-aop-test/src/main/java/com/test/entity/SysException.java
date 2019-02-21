package com.test.entity;

import com.test.util.SysMessageEnum;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-29
 */
public class SysException extends RuntimeException {
    private Integer code;
    private Exception e;

    public SysException(SysMessageEnum sysMessageEnum) {
        super(sysMessageEnum.getMsg());
        this.code = sysMessageEnum.getCode();
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
