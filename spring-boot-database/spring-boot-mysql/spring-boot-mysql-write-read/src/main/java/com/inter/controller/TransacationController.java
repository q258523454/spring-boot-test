package com.inter.controller;

import com.inter.service.StudentService;
import com.inter.service.TeacherService;
import com.inter.service.TransactionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TransacationController {

    private Logger log = LoggerFactory.getLogger(TransacationController.class);


    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TransactionalService transactionalService;


    // 请确认注解没有放到 DAO 层方法上, 因为会在 Service 层开启事务，所以当注解在 DAO 层时不会生效
    @RequestMapping(value = "/transactionOrg", method = RequestMethod.GET)
    public String transactionOrg() throws Exception {
        transactionalService.transactionOrg();
        return "1";
    }


}

