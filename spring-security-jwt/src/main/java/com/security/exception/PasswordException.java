package com.security.exception;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-11-06
 */
public class PasswordException extends BaseRuntimeException {
    public PasswordException(String message) {
        super(message);
    }

    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
