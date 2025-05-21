package com.trans.service.impl;

import com.trans.entity.Student;
import com.trans.service.StudentService;
import com.trans.service.TransactionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class TransactionalSelectUpdateInvalid {

    private Logger logger = LoggerFactory.getLogger(TransactionalService.class);

    @Autowired
    private StudentService studentService;


    // FIXME: 直接去 test包目录下执行 @Test
    public void select(){
    }

}
