package com.ratelimitbase.exception;

import lombok.Getter;

/**
 * RateLimiterException
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Getter
public class RateLimiterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * code
     */
    private final Integer code;

    /**
     * 报错信息 英文
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    code
     * @param message message
     */
    public RateLimiterException(Integer code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param errorCode errorCode
     */
    public RateLimiterException(RateLimiterErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * Instantiates a new Form exception.
     *
     * @param errorCode errorCode
     * @param message   message
     */
    public RateLimiterException(RateLimiterErrorCode errorCode, String message) {
        this(errorCode.getCode(), message);
    }

    @Override
    public String getMessage() {
        return code + "-" + super.getMessage();
    }
}
