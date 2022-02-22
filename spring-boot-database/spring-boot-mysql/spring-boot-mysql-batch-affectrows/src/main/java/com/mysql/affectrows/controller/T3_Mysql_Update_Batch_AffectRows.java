package com.mysql.affectrows.controller;

import com.mysql.affectrows.entity.Student;
import com.mysql.affectrows.serivce.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @date 2020-04-26 11:22
 * @modify
 */
@RestController
public class T3_Mysql_Update_Batch_AffectRows {
    private static final Logger logger = LoggerFactory.getLogger(T3_Mysql_Update_Batch_AffectRows.class);

    @Autowired
    private IStudentService studentService;

    /**
     * update batch{foreach 下 case when} 格式可以返回影响行数
     * 注意: 同insert一样,begin-end MYSQL只能用于存储过程
     *
     * @param num 从最大值开始，依次往前更新多少个, 默认一次更新2个
     */
    @GetMapping(value = "/affect/rows/update/batch")
    public String batch(Integer num) {
        List<Student> list = new ArrayList<>();
        if (null == studentService.getMaxId()) {
            return "no data to delete.";
        }
        long maxId = studentService.getMaxId();
        if (num == null) {
            num = 2;
        }
        for (long i = maxId - num + 1; i <= maxId; i++) {
            Student student = getStudent(i + "");
            student.setAge(num);
            list.add(student);
        }

        int i = 0;
        if (num % 2 != 0) {
            // 单条件 update batch
            i = studentService.updateListByIdBatch(list);
        } else {
            // 多条件 update batch (这里为了方便模拟,两个条件都是id,实际情况换成不同条件即可)
            // 多条件用in在索引不全的情况下要由于or
            i = studentService.updateListByIdAndIdBatch(list);
        }
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    // mysql:begin end 只能用在存储过程
    @GetMapping(value = "/affect/rows/update/beginend")
    public String updateBeginEnd() {
        return "mysql begin end only create procedure.";
    }

    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.getMaxId() + 1);
        }
        Student student = new Student();
        student.setId(Integer.valueOf(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(20);
        return student;
    }
}
