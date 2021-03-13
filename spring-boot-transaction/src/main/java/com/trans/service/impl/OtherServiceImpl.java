package com.trans.service.impl;

import com.trans.service.OtherService;
import com.trans.service.TransactionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */
@Service
public class OtherServiceImpl implements OtherService {
    @Autowired
    private TransactionalService transactionalService;


    @Override
    public void publicCallPrivateWhichCallSpringBeanTrans() {
        privateMethodCallSpringBeanTrans();
    }

    @Transactional
    public void privateMethodCallSpringBeanTrans() {
        transactionalService.publicMethod();
    }

    @Override
    public void ordinaryMethod_require_new() throws Exception {
        transactionalService.publicMethod_REQUIRES_NEW();
    }

    @Override
    public void ordinaryMethod_not_supported() throws Exception {
        transactionalService.publicMethod_NOT_SUPPORTED();
    }
}
