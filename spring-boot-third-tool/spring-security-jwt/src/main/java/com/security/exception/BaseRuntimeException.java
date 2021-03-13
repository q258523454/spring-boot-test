
package com.security.exception;

public class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

