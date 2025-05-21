package com.inter.entity.excpetion;

/**
 * @Date: 2019-05-27
 * @Version 1.0
 */
// 自定义业务异常类
public class BusinessException extends Exception {
    private static final long serialVersionUID = -4762181054514075995L;

    private Integer code;

    // 自定义构造器，必输错误码及内容
    public BusinessException(Integer code, String msg) {
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
