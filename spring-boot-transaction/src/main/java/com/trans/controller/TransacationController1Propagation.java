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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-25
 */

@Slf4j
@RestController
public class TransacationController1Propagation {
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
     * Propagation.NOT_SUPPORTED:   以非事务方式执行操作，存在则先挂起
     * Propagation.REQUIRED:        没有事务就新建，存在则沿用
     * Propagation.REQUIRES_NEW:    没有事务就新建，存在则挂起再新建
     * Propagation.NESTED:          如果当前存在事务，则在嵌套事务内执行。不存在则=PROPAGATION_REQUIRED
     *
     * 小结：
     *      内层异常不影响外层
     *      如果想外部事务不受内部异常的影响，内层可用required_new和required_nested
     *      外层异常不影响内层
     *      如果想内部事务不受外部事务异常的影响，内层可用required_new和not_supported
     */

    // 非事务方法调用事务方法, 事务方法中的异常只回滚事务方法, 外层方法没有事务, 不回滚.
    @RequestMapping(value = "/transaction1", method = RequestMethod.GET)
    public String transaction1() throws Exception {
        Student student = new Student("transaction1", "123", new Date());
        Teacher teacher = new Teacher("transaction1", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 不回滚
        teacherMapper.insertTeacher(teacher); // ② 不回滚
        transactionalService.publicMethod();  // ③ 回滚
        return "1";
    }


    // 在事务方法内调用非事务方法, 出现异常全部回滚
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_NO_TRANSCATIONAL", method = RequestMethod.GET)
    public String propagationTest_SUB_NO_TRANSCATIONAL() throws Exception {
        Student student = new Student("propagationTest_SUB_NO_TRANSCATIONAL", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_NO_TRANSCATIONAL", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        transactionalService.publicMethod_NO_TRANSCATIONAL();  // ③ 回滚
        return "1";
    }

    // 在事务方法内调用非事务方法, 出现异常,如果捕获了, 不会回滚
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_NO_TRANSCATIONAL2", method = RequestMethod.GET)
    public String propagationTest_SUB_NO_TRANSCATIONAL2() throws Exception {
        Student student = new Student("propagationTest_SUB_NO_TRANSCATIONAL", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_NO_TRANSCATIONAL", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 不回滚
        teacherMapper.insertTeacher(teacher); // ② 不回滚
        try {
            transactionalService.publicMethod_NO_TRANSCATIONAL();  // ③ 不回滚
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "1";
    }

    // 在事务方法内调用事务方法(REQUIRED)无论哪个方法出现异常都全部回滚,即使捕获了内部是方法的异常.
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_NO_TRANSCATIONAL3", method = RequestMethod.GET)
    public String propagationTest_SUB_NO_TRANSCATIONAL3() throws Exception {
        Student student = new Student("propagationTest_SUB_NO_TRANSCATIONAL", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_NO_TRANSCATIONAL", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        try {
            transactionalService.publicMethod_REQUIRED();  // ③ 回滚
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "1";
    }

    // 在事务方法内调用事务方法(NOT_SUPPORTED), 出现异常只回滚外层
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_NOT_SUPPORTED", method = RequestMethod.GET)
    public String propagationTest_SUB_NOT_SUPPORTED() throws Exception {
        Student student = new Student("propagationTest_SUB_NOT_SUPPORTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_NOT_SUPPORTED", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student);               // ① 回滚
        teacherMapper.insertTeacher(teacher);               // ② 回滚
        transactionalService.publicMethod_NOT_SUPPORTED();  // ③ 不回滚
        return "1";
    }

    // 事务方法->普通方法->事务方法 (效果同 事务方法->普通方法)
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_ORDINARY_TRANS", method = RequestMethod.GET)
    public String propagationTest_SUB_ORDINARY_TRANS() throws Exception {
        Student student = new Student("propagationTest_SUB_NOT_SUPPORTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_NOT_SUPPORTED", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student);               // ①
        teacherMapper.insertTeacher(teacher);               // ②
        otherService.ordinaryMethod_not_supported();        // ③
        return "1";
    }

    // --------------------------------------------- REQUIRES_NEW ---------------------------------------------
    // 事务方法-REQUIRES_NEW 如果REQUIRES_NEW事务方法内部出现异常, 且没有处理, 则一起回滚
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_NEW", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_NEW() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NEW", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NEW", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        // ③出现异常, 虽然③是新建事务, 但是没有处理③的异常, 导致①和②也要回滚
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        transactionalService.publicMethod_REQUIRES_NEW();  // ③ 回滚
        return "1";
    }

    // 事务方法-REQUIRES_NEW 如果REQUIRES_NEW事务方法内部出现异常, 如果有处理, 则只回滚新建的REQUIRES_NEW事务方法
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_NEW_TryCatch", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_NEW_TryCatch() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NEW", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NEW", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        // ③出现异常, 但③是新建事务, 且捕获了③异常, ①和②不会回滚
        studentMapper.insertStudent(student); // ① 不回滚
        teacherMapper.insertTeacher(teacher); // ② 不回滚
        try {
            transactionalService.publicMethod_REQUIRES_NEW();  // ③ 回滚
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }

    // --------------------------------------------- NESTED ---------------------------------------------
    // 事务方法-NESTED 外层失败,内层成功: 同时回滚 (外层影响内层),REQUIRED_NEW做不到
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_NESTED", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_NESTED() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NESTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NESTED", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        // 外层的方法失败，则会连同内部方法一起回滚(因为它是NESTED-嵌套事务)
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        transactionalService.publicMethod_NESTED_NO_ERROR();  // ③ 回滚 (NESTED嵌套子事务)
        MultiByZero.multiByZero();
        return "1";
    }

    // 事务方法-NESTED 外层失败,内层失败: 都回滚
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_NESTED2", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_NESTED2() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NESTED2", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NESTED2", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        try {
            // REQUIRED(默认), 回滚(回滚嵌套事务)
            transactionalService.publicMethod_NESTED();  // ③ 回滚, 效果等价于REQUIRED_NEW
        } catch (Exception e) {
            // 处理了③异常
            e.printStackTrace();
        }
        MultiByZero.multiByZero();
        return "1";
    }

    // 事务方法-NESTED 外层成功，内层失败: 外层不受影响， 内层回滚 (内层不影响外层),，REQUIRED 做不到
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_NESTED3", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_NESTED3() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NESTED3", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NESTED3", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 不回滚
        teacherMapper.insertTeacher(teacher); // ② 不回滚
        try {
            // 如果是REQUIRED(默认), 回滚(回滚嵌套事务)
            transactionalService.publicMethod_NESTED();  // ③ 回滚, 此时效果等价于REQUIRED_NEW
        } catch (Exception e) {
            // 处理了③异常
            e.printStackTrace();
        }
        return "1";
    }


    // ---------------------------  REQUIRES_NEW 和 NESTED[强调嵌套] 区别 ---------------------------
    // 当为 事务方法-REQUIRES_NEW 时   外层不会影响内层: 如果内层方法成功, 但是外层失败，内层方法则不会回滚(因为它是新建的事务)
    // 当为 事务方法-NESTED 时         外层一定影响内层: 如果内层方法成功, 但是外层失败，则会连同内部方法一起回滚
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_NEW_AND_NESTED", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_NEW_AND_NESTED() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NEW_AND_NESTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NEW_AND_NESTED", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        transactionalService.publicMethod_REQUIRES_NEW_NO_ERROR();  // ③ 不回滚
        MultiByZero.multiByZero();
        return "1";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_NEW_AND_NESTED2", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_NEW_AND_NESTED2() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_NEW_AND_NESTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_NEW_AND_NESTED", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        // 外层的方法失败，则会连同内部方法一起回滚(因为它是NESTED-嵌套事务)
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        transactionalService.publicMethod_NESTED_NO_ERROR();  // ③ 回滚 (NESTED嵌套子事务)
        MultiByZero.multiByZero();
        return "1";
    }

    // ---------------------------  REQUIRES(默认) 和 NESTED[强调嵌套] 区别 ---------------------------
    // 当为 事务方法-REQUIRES时    如果内层方法失败, 但是外层成功，即使我们在外部方法中捕获异常，外部的事务也会全部回滚（整个事务都不成功)
    // 当为 事务方法-NESTED 时     如果内层方法失败, 但是外层成功，则只会回滚嵌套事务，外部事务不受影响
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_AND_REQUIRED", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_AND_REQUIRED() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_AND_NESTED", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_AND_NESTED", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 回滚
        try {
            //
            /**
             * REQUIRED(默认),全部回滚(回滚整个事务)
             * 回滚, 注意:
             * 1.如果下面的方法是已执行的SQL普通方法事务不会回滚
             * 2.如果是(REQUIRED_NEW),①和②不会回滚
             */
            transactionalService.publicMethod_REQUIRED();  // ③ 回滚
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/propagationTest_SUB_REQUIRES_AND_NESTED", method = RequestMethod.GET)
    public String propagationTest_SUB_REQUIRES_AND_NESTED() throws Exception {
        Student student = new Student("propagationTest_SUB_REQUIRES_AND_NESTED2", "123", new Date());
        Teacher teacher = new Teacher("propagationTest_SUB_REQUIRES_AND_NESTED2", "123", new Date());
        log.info(JSONObject.toJSONString(student));
        log.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 不回滚
        teacherMapper.insertTeacher(teacher); // ② 不回滚
        try {
            // REQUIRED(默认), 回滚(回滚嵌套事务)
            transactionalService.publicMethod_NESTED();  // ③ 回滚, 效果等价于REQUIRED_NEW
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }
}
