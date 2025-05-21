package com.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.dao.StudentMapper;
import com.trans.dao.TeacherMapper;
import com.trans.entity.Student;
import com.trans.entity.Teacher;
import com.trans.service.OtherService;
import com.trans.service.StudentService;
import com.trans.service.TeacherService;
import com.trans.service.TransactionalService;
import com.trans.util.MultiByZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class TransacationController2ManCommit {

    private Logger logger = LoggerFactory.getLogger(TransacationController2ManCommit.class);

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TransactionalService transactionalService;

    @Autowired
    private OtherService otherService;


    /**
     * 在事务方法内调用 NOT_SUPPORTED 事务方法, 出现异常只回滚外层
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationtest_not_supported_self", method = RequestMethod.GET)
    public String PROPAGATIONTEST_NOT_SUPPORTED_MAN() throws Exception {
        Student student = new Student("propagationTest_SUB_NOT_SUPPORTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_NOT_SUPPORTED", "123", new Date());
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student);                   // ① 回滚
        teacherMapper.insertTeacher(teacher);                   // ② 回滚
        transactionalService.publicMethod_NOT_SUPPORTED_MAN(); // ③ 不回滚
        return "1";
    }

    /**
     * 事务方法-NESTED 内层成功,外层失败: 同时回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagation_requires_nested_self", method = RequestMethod.GET)
    public String PROPAGATION_REQUIRES_NESTED_SELF() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NESTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NESTED", "123", new Date());
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        // 外层的方法失败，则会连同内部方法一起回滚(因为它是NESTED-嵌套事务)
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        transactionalService.publicMethod_NESTED_MAN();  // ③ 回滚 (NESTED嵌套子事务)
        MultiByZero.multiByZero();
        return "1";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagation_requires_nested_self2", method = RequestMethod.GET)
    public String PROPAGATION_REQUIRES_NESTED_SELF2() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NESTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NESTED", "123", new Date());
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        // 外层的方法失败，则会连同内部方法一起回滚(因为它是NESTED-嵌套事务)
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        transactionalService.publicMethod_NESTED_NO_ERROR_MAN();  // ③ 回滚 (NESTED嵌套子事务)
        MultiByZero.multiByZero();
        return "1";
    }
}
