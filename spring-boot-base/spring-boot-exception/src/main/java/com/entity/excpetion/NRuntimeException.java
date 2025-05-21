package com.inter.entity.excpetion;

/**
 * @Date: 2019-06-10
 * @Version 1.0
 */
public class NRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -4762181054514075002L;

    private Integer code;

    // 自定义Exception, 默认code=500
    public NRuntimeException(String msg) {
        super(msg);
        this.code = 500;
    }

    public NRuntimeException(Integer code, String msg) {
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
