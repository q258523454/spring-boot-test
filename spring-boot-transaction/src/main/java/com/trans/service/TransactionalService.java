package com.trans.service;


public interface TransactionalService {
    void publicMethod();

    void callOwn_REQUIRES_NEW_NO_ERROR();

    void callOwnPublicMethod();

    void transInMethod();

    void publicMethod_NO_TRANSCATIONAL();

    void publicMethod_REQUIRED() throws Exception;

    void publicMethod_REQUIRED_NO_ERROR();

    void publicMethod_NOT_SUPPORTED() throws Exception;

    void publicMethod_NOT_SUPPORTED_MAN() throws Exception;

    void publicMethod_REQUIRES_NEW() throws Exception;

    void publicMethod_REQUIRES_NEW_MAN() throws Exception;

    void publicMethod_REQUIRES_NEW_NO_ERROR();

    void publicMethod_NESTED();

    void publicMethod_NESTED_NO_ERROR();

    void publicMethod_NESTED_MAN();

    void publicMethod_NESTED_NO_ERROR_MAN();


}
