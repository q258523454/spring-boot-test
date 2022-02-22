package com.oracle.vs.mybatis.controller;

import com.oracle.vs.jdbc.T2_Run2_JDBC_Insert_Batch;
import com.oracle.vs.mybatis.dao.OracleStudentMapper;
import com.oracle.vs.mybatis.entity.Student;
import com.oracle.vs.mybatis.service.IOracleStudentService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@RestController
public class OracleControllerInsertBatch {
    private static final Logger logger = LoggerFactory.getLogger(OracleControllerInsertBatch.class);

    @Autowired
    private IOracleStudentService studentService;

    /**
     * 默认 executor-type: simple
     */
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private Random random = new Random();

    /**
     * 总结:
     *  不考虑返回行数
     *  <5万条数据: 事务下循环单条操作效率 ≈ JDBC原生batch-prepared {@link T2_Run2_JDBC_Insert_Batch}
     *  >5万条数据: 用 JDBC原生batch-prepared
     *  如果想要返回行数, 只能用 JDBC原生batch-prepared.
     */


    /**
     * 事务下,循环单次单条插入(有Spring事务管理), 开启 batch 配置
     * default-executor-type: batch
     */
    @Transactional // 必须加上,否则无法用到 spring 的 sqlSession
    @GetMapping(value = "/student/insert/for-one")
    public String one(int num) {
        long start = System.currentTimeMillis();
        Long maxId = studentService.getMaxId();
        long tempMaxId = (maxId == null) ? 0 : studentService.getMaxId() + 1;

        for (int i = 0; i < num; i++) {
            long pId = tempMaxId + i;
            Student student = getStudent(pId + "");
            studentService.insert(student);
        }
        long end = System.currentTimeMillis();
        logger.info("批量插入 执行时间:" + (end - start));
        return "ok";
    }


    /**
     * 事务下,循环单次单条插入(有Spring事务管理), 开启 batch
     * 测试可能造成数据库有缓存,且batch有预编译,为了准确, 每次测试,重启应用,重新建表
     * 自定义 executor-type: batch (效果同配置开启)
     * 1000条: 31 31 33 ms
     * 3000条: 77 71 139 ms
     * 5000条: 296 114 111 ms
     * 10000条: 227,163,168 ms
     * 50000条: 1007,892,829 ms
     * 100000条: 2004,1699,1739 ms
     * JDBC batch-prepared 参考{@link T2_Run2_JDBC_Insert_Batch}
     */
    @Transactional // 加不加无影响
    @GetMapping(value = "/student/insert/foreachbatch/sqlsession/batch")
    public String batch(int num) {
        // 不测试 simple , 它跟循环单条效果一样
        // 创建:SimpleExecutor 或 BatchExecutor,
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        long start = System.currentTimeMillis();
        try {
            OracleStudentMapper mapper = sqlSession.getMapper(OracleStudentMapper.class);
            Long maxId = mapper.getMaxId();
            long tempMaxId = (maxId == null) ? 0 : maxId + 1;
            for (int i = 0; i < num; i++) {
                long pId = tempMaxId + i;
                Student student = getStudent(pId + "");
                mapper.insert(student);
            }
            sqlSession.commit();
        } catch (Exception ex) {
            sqlSession.rollback();
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
        long end = System.currentTimeMillis();
        logger.info("批量插入 执行时间:" + (end - start));
        return "ok";
    }

    public Student getStudent(String id) {
        Student student = new Student();
        student.setId(new BigDecimal(id));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("" + random.nextInt(9999)));
        return student;
    }
}
