package shiro2.crazycake.shiro.exception;

import java.text.MessageFormat;

public class PrincipalIdNullException extends RuntimeException {

    private static final String MESSAGE = "Principal Id shouldn't be null!";

    public PrincipalIdNullException(Class clazz, String idMethodName) {
        super(MessageFormat.format(" {0} id field: {1}, value is null, {2}", clazz, idMethodName, MESSAGE));
    }
}
