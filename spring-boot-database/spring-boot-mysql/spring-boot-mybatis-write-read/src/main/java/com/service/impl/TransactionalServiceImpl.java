package com.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dao.StudentMapper;
import com.dao.TeacherMapper;
import com.datasource.annotation.DataSourceAnnotation;
import com.datasource.entity.DataSourceEnum;
import com.datasource.util.MultiByZeroExceptionUtil;
import com.entity.Student;
import com.entity.Teacher;
import com.service.TransactionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */

@Service
public class TransactionalServiceImpl implements TransactionalService {

    private static final Logger log = LoggerFactory.getLogger(TransactionalServiceImpl.class);

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;


    // 注意下面的事务只能对一个数据源,保持一个service中操作一个数据源的原则,不同源的数据库是不能同时进行事务操作的
    @Override
    @DataSourceAnnotation(DataSourceEnum.SLAVE) // @DataSourceAnnotation可以不写, 默认master数据源
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String transactionOrg() throws Exception {
        Student student1 = new Student("student_transactionOrg1", "123", new Date());
        Student student2 = new Student("student_transactionOrg2", "123", new Date());

        String beanJson1 = JSON.toJSONString(student1);
        String beanJson2 = JSON.toJSONString(student2);
        log.info(beanJson1);
        log.info(beanJson2);

        Teacher teacher1 = new Teacher("teacher_transactionOrg1", "abc", new Date());
        Teacher teacher2 = new Teacher("teacher_transactionOrg2", "abc", new Date());
        beanJson1 = JSON.toJSONString(teacher1);
        beanJson2 = JSON.toJSONString(teacher2);
        log.info(beanJson1);
        log.info(beanJson2);

//        studentService.insertStudent(student1); // ①
        teacherMapper.insertTeacher(teacher1); // ①
        MultiByZeroExceptionUtil.multiByZero(0);  // 出现ArithmeticException异常, ①不会插入,②不会插入，因为有@Transactional
//        studentService.insertStudent(student2); // ②
        teacherMapper.insertTeacher(teacher2); // ②
        return "1";
    }

}
