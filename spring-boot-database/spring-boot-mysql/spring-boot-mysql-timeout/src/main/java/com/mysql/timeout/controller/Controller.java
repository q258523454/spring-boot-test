package com.mysql.timeout.controller;

import com.alibaba.fastjson.JSON;
import com.mysql.timeout.entity.Student;
import com.mysql.timeout.serivce.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;



@Slf4j
@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private IStudentService studentService;

    /**
     * 总结：@Transactional 中的 timeout 只有在当前事务下执行数据库操作的时候才会计算和生效
     * 1.例如外层事务设置超时: @Transactional(timeout = 2)
     *   内层(require_new)timeout = 3 只会影响当前事务超时时间
     * 2.设置实际sql执行超时: default-statement-timeout
     *   XML中 单条SQL 执行的超时时间, 可在mapper.xml对sql单独设置
     */

    @GetMapping(value = "/insert")
    public String insert() {
        Student student = getStudent("");
        return JSON.toJSONString(studentService.insert(student));
    }

    /**
     *
     * @Transactional(timeout = 2) 单位s
     */
    @GetMapping(value = "/insertTimeout")
    public String insertTimeout() {
        Student student = getStudent("");
        return JSON.toJSONString(studentService.insertTimeout(student));
    }

    @GetMapping(value = "/insertTimeoutNotWork1")
    public String insertTimeoutNotWork1() throws InterruptedException {
        Student student = getStudent("");
        // 设置超时时间3秒,内部超时,但是先执行了 Statement,超时设置无效
        studentService.insertTimeoutNotWork1(student);
        return "ok";
    }

    @GetMapping(value = "/insertTimeoutNotWork2")
    public String insertTimeoutNotWork2() throws InterruptedException {
        Student student = getStudent("");
        // 设置超时时间3秒,内部未超时,超时设置无效
        Thread.sleep(3000);
        studentService.insertTimeoutNotWork2(student);
        return "ok";

    }

    /**
     * 超时时间只有在当前事务下执行数据库操作的时候才会计算，并生效
     * 计算公式=@Transactional开启到最后一个Statement的提交时间
     * 例如:下面外层的 REQUIRES_NEW 会超时, 但是内嵌的 REQUIRES_NEW 不会.
     * 外层 REQUIRES_NEW 在外层操作数据库 studentService.insert(student) 的时候才发出超时计算
     *
     */
    @GetMapping(value = "/insertTimeoutWithRequireNewTimeout")
    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 3)
    public String insertTimeoutWithRequireNewTimeout() {
        Student student = getStudent("");
        int i = studentService.insertTimeoutRequireNew(student);
        student.setId(student.getId() + 1);
        int insert = studentService.insert(student);
        return i + "";
    }


    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.selectMaxId() + 1);
        }
        Student student = new Student();
        student.setId(Integer.valueOf(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(Integer.valueOf(("20")));
        return student;
    }
}
