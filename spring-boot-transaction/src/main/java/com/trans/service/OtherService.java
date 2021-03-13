package com.trans.service;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */
public interface OtherService {
    public void publicCallPrivateWhichCallSpringBeanTrans();

    public void ordinaryMethod_require_new() throws Exception;

    public void ordinaryMethod_not_supported() throws Exception;

}
