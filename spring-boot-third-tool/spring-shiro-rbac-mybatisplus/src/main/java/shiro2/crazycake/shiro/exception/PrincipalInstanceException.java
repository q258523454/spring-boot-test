package shiro2.crazycake.shiro.exception;

import java.text.MessageFormat;

public class PrincipalInstanceException extends RuntimeException {


    private static final String MESSAGE = "We need a field to identify this Cache Object in Redis. "
            + "So you need to defined an id field which you can get unique id to identify this principal. "
            + "For example, if you use UserInfo as Principal class, the id field maybe userId, userName, email, etc. "
            + "For example, getUserId(), getUserName(), getEmail(), etc."
            + System.lineSeparator()
            + "Default value is authCacheKey or id, that means your principal object has a method called \"getAuthCacheKey()\" or \"getId()\"";

    public PrincipalInstanceException(Class clazz, String idMethodName) {
        super(MessageFormat.format("{0} must has getter for field:{1}.{2}{3}", clazz, idMethodName, System.lineSeparator(), MESSAGE));
    }

    public PrincipalInstanceException(Class clazz, String idMethodName, Exception e) {
        super(MessageFormat.format("{0} must has getter for field:{1}.{2}{3}", clazz, idMethodName, System.lineSeparator(), MESSAGE), e);
    }
}
