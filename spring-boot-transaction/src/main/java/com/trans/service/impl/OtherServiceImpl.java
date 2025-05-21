package com.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trans.entity.Student;
import com.trans.service.OtherService;
import com.trans.service.StudentService;
import com.trans.service.TransactionalService;
import com.trans.util.MultiByZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OtherServiceImpl implements OtherService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TransactionalService transactionalService;


    @Override
    public void publicCallPrivateWhichCallSpringBeanTrans() {
        privateMethodCallSpringBeanTrans();
    }

    public void privateMethodCallSpringBeanTrans() {
        transactionalService.publicMethod();
    }

    @Override
    public void ordinaryMethod_require_new() throws Exception {
        transactionalService.publicMethod_REQUIRES_NEW();
    }

    @Override
    public void ordinaryMethod_call_not_supported() throws Exception {
        transactionalService.publicMethod_NOT_SUPPORTED();
    }
}
