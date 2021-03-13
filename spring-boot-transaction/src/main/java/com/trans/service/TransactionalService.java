package com.trans.service;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */
public interface TransactionalService {
    void publicMethod();

    void callOwnPublicMethod();

    void transInMethod();

    void publicMethod_NO_TRANSCATIONAL();

    void publicMethod_REQUIRED() throws Exception;

    void publicMethod_REQUIRED_NO_ERROR() throws Exception;

    void publicMethod_NOT_SUPPORTED() throws Exception;

    void publicMethod_NOT_SUPPORTED_MAN() throws Exception;

    void publicMethod_REQUIRES_NEW() throws Exception;

    void publicMethod_REQUIRES_NEW_MAN() throws Exception;

    void publicMethod_REQUIRES_NEW_NO_ERROR() throws Exception;

    void publicMethod_NESTED();

    void publicMethod_NESTED_NO_ERROR();

    void publicMethod_NESTED_MAN();

    void publicMethod_NESTED_NO_ERROR_MAN();


}
