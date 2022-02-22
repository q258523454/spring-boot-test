package com.batch.controller.effective;

import com.batch.controller.afftect_rows.T3_Update_Batch_AffectRows;
import com.batch.entity.Student;
import com.batch.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @date 2020-04-26 11:22
 * @modify
 */
@RestController
public class T0_Update_Effective_test {
    private static final Logger logger = LoggerFactory.getLogger(T0_Update_Effective_test.class);

    @Autowired
    private IOracleStudentService studentService;

    /**
     * 结论:
     * --------------------------------------------------------------------------------------------------
     *                                       1000条时间   备注
     * 单次单条(不参与测试,直接排除)
     * 单次单条 update 批量(foreach)          85  ms    不能超过1000条,否则报错:ORA-01795,可以分组执行; 注意null值的坑,
     * 单次多条 update 批量(BEGIN END)        100 ms     SQL本身具有事务性
     * 参考 {@link T3_Update_Batch_AffectRows}
     * --------------------------------------------------------------------------------------------------
     *
     * update的 foreach-batch 不能超过 1000条
     * <1000条,用 foreach-batch
     * >1000条,用 事务下循环单条操作
     *
     */

    /**
     * 推荐写法,batch,具体参考
     * {@link T3_Update_Batch_AffectRows}
     * 1000条: 418,75,91
     *
     * 不能超过1000条,否则报错:ORA-01795
     */
    @GetMapping(value = "/effective/student/update/batch")
    public String batch(Integer num) {
        long start = System.currentTimeMillis();

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
        int i = 0;
        // 单条件 update batch
        i = studentService.updateListByIdBatch(list);
        logger.info("Mybatis SQL return :" + i);
        long end = System.currentTimeMillis();
        System.out.println("batch 执行时间:" + (end - start));
        return i + "";
    }

    /**
     * 1000条: 801,83,81
     * 2000条: 2284,174,156
     * 5000条: 17093,476,520
     */
    @GetMapping(value = "/effective/student/update/beginend")
    public String updateBeginEnd(Integer num) {
        long start = System.currentTimeMillis();

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
        long end = System.currentTimeMillis();
        System.out.println("beginend 执行时间:" + (end - start));
        return i + "";
    }


    /**
     * 1000条: 1254,1346,1215
     * 2000条: 2395,2611,2655
     * 5000条: 5907,6583,6512
     */
    @Transactional
    @GetMapping(value = "/effective/student/update/fori/trans")
    public String fori(Integer num) {
        long start = System.currentTimeMillis();

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
            studentService.update(student);
        }
        long end = System.currentTimeMillis();
        System.out.println("batch 执行时间:" + (end - start));
        return "ok";
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
