package com.batch.controller.afftect_rows;

import com.batch.entity.Student;
import com.batch.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @date 2020-04-26 11:22
 */
@RestController
public class T3_Update_Batch_AffectRows {
    private static final Logger logger = LoggerFactory.getLogger(T3_Update_Batch_AffectRows.class);

    @Autowired
    private IOracleStudentService studentService;

    /**
     * update batch{foreach + case when} 格式可以返回影响行数
     * 注意: 同insert一样. begin end 无法返回更新行数
     * @param num 从最大值开始，依次往前更新多少个, 默认一次更新2个
     *
     * 坑: case-when的写法, 如果部分输入字段为null,则对应数据库字段重置为null。 因此需要加上 choose-when-otherwise 来
     * 做一次用原值自我更新的操作
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
            // 当某个字段存在空和非空两种数据
            if (i % 2 == 0) {
                Student student = getStudent(i + "");
                student.setAge(BigDecimal.valueOf(num));
                list.add(student);
            } else {
                Student student = getStudent(i + "");
                student.setName(null);
                student.setAge(BigDecimal.valueOf(num));
                list.add(student);
            }
        }
        int i = 0;
        if (num % 2 != 0) {
            // 单条件 update batch
            i = studentService.updateListByIdBatch(list);
        } else {
            // 多条件 update batch (这里为了方便模拟,两个条件都是id,实际情况换成不同条件即可)
            i = studentService.updateListByIdAndIdBatch(list);
        }
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }


    @GetMapping(value = "/affect/rows/update/beginend")
    public String updateBeginEnd(Integer num) {
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
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        int i = studentService.updateListByIdBeginEnd(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }


    // ------------------------------------------ 其他测试 ------------------------------------------
    @GetMapping(value = "/affect/rows/update/batch/mixnull")
    public String mixNull(Integer num) {
        List<Student> list = new ArrayList<>();
        if (null == studentService.getMaxId()) {
            return "no data to delete.";
        }
        long maxId = studentService.getMaxId();
        if (num == null) {
            num = 2;
        }
        for (long i = maxId - num + 1; i <= maxId; i++) {
            // 不规则非空字段混合着更新
            if (i % 2 == 0) {
                Student student = getStudent(i + "");
                student.setName(null);
                list.add(student);
            } else {
                Student student = getStudent(i + "");
                student.setAge(null);
                list.add(student);
            }
        }
        int i = 0;
        i = studentService.updateListByIdBatch(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.getMaxId() + 1);
        }
        Student student = new Student();
        student.setId(new BigDecimal(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("20"));
        return student;
    }

}
