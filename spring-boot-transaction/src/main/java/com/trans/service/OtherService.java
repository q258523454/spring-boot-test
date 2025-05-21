package com.trans.service;


public interface OtherService {
    public void publicCallPrivateWhichCallSpringBeanTrans();

    public void ordinaryMethod_require_new() throws Exception;

    public void ordinaryMethod_call_not_supported() throws Exception;

}
