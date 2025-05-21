package com.inter.entity.excpetion;

/**
 * @Date: 2019-05-27
 * @Version 1.0
 */
// 自定义Exception异常类
public class NException extends Exception {
    private static final long serialVersionUID = -4762181054514075001L;

    private Integer code;

    // 自定义Exception, 默认code=500
    public NException(String msg) {
        super(msg);
        this.code = 500;
    }

    public NException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
