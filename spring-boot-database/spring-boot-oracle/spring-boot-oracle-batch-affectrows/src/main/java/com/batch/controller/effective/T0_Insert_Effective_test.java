package com.batch.controller.effective;

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
import java.util.Random;
import java.util.UUID;



@RestController
public class T0_Insert_Effective_test {
    private static final Logger logger = LoggerFactory.getLogger(T0_Insert_Effective_test.class);

    @Autowired
    private IOracleStudentService studentService;

    private Random random = new Random();


    /**
     *
     * 前言:
     * default-executor-type:
     *  simple: SimpleExecutor, 单个 sqlsession 内, 每次操作，都开启一个 Statement 对象，用完立刻关闭 Statement 对象
     *  batch: BatchExecutor, 单个 sqlsession 内,每次操作复用已有 Statement 对象, addBatch()汇总，然后统一执行executeBatch()
     *                        因此 Mybatis 官方写明它无法返回行数(BATCH executor is in use, the update counts are being lost.)
     *  reuse: ReuseExecutor, 应用实例内, 全局共享 Statement对象(Map<String, Statement>), 存在则复用
     *
     * 关于 statement
     * Statement 可以正常访问数据库，适用于运行静态 SQL 语句。 Statement 接口不接受参数。
     * PreparedStatement 计划多次使用 SQL 语句， PreparedStatement 接口运行时接受输入的参数。
     *
     * 测试结果:
     * --------------------------------------------------------------------------------------------------
     *                                              备注
     * 无事务,循环单次单条插入(无Spring事务管理)        多次创建SqlSession
     * 事务下,循环单次单条插入(有Spring事务管理)        单次创建SqlSession, Spring 循环的时候,复用了上一次SqlSession
     * 单次多条insert语句,批量插入(BEGIN END)        单次创建,有无 @Transactional 对效率影响不大, 注意SQL本身具有事务性
     * 单次单条insert语句,批量插入(foreach batch)    单次创建,有无 @Transactional 对效率影响不大, 注意SQL本身具有事务性
     *--------------------------------------------------------------------------------------------------
     * items	                                1000 条	        2500 条	            5000 条
     * 非事务,循环单次单条插入	                    449,331,744,109	113,911,108,813,093	-
     * 有事务,循环单次单条插入(Simple模式 )	        195,613,441,224	34,333,933,460	    596,757,005,844
     * 批量操作-BEGIN END	                        819,103,102	    3,344,219,237	    18,312,652,462
     * 批量操作-foreach batch	                    4,534,539	    406,339,681	        35,399,174,195
     *--------------------------------------------------------------------------------------------------
     * 	                                        1万条	        5万条	            10万条
     * JDBC原生batch-prepared	                2,811,297,299	11,321,065,735	    713,758,886
     * 有事务,循环单次单条插入(Batch模式)	        227,163,168	    1,007,892,829	    200,416,991,739
     * --------------------------------------------------------------------------------------------------
     *
     * 结论：
     * 1.foreach-batch 和 BEGIN END 受 Oracle 数据库缓存影响,增删改第一次执行后,效率明显提升。
     *  取决于数据库, 重启应用依然有效。我们平常用的最多的写法应该就是 foreach-batch
     * 2.simple(default-executor-type) 模式:
     *  <2000条,用 foreach-batch(注意:foreach-batch update写法,不能超过1000条)
     *  >2000条,用 事务下循环单条操作
     *  原因分析:SQL拼接字段太多, 硬解析 会耗时耗资源.此时,事务下循环单条操作,会复用模板的软解析,反而效率更高
     *          大量的硬解析可能会导致 Oracle:ORA-04036 PGA memory 错误.
     * 3.batch(default-executor-type) 模式:
     *  不考虑返回行数
     *  <5万条数据: 事务下循环单条操作效率 ≈ JDBC原生batch-prepared
     *  >5万条数据: 用 JDBC原生batch-prepared {@link com/oracle/vs/jdbc/T2_Run2_JDBC_Insert_Batch}
     *  如果batch下也想要返回行数, 只能用 JDBC原生batch-prepared.
     */

    /**
     * ★★★<2000条,推荐写法★★★
     * 单次单条insert语句, 批量插入(foreach batch)写法是单条SQL(本身具有事务性),默认配置下,不受 simple/batch 影响
     * 1000 条: 453,45,39
     * 2500 条: 4063,396,81
     * 3000 条: 6409 110 113
     * 5000 条: 35399,174,195
     * 6000 条: 68472 284 163
     * 10000条: 214747,302,311
     */
    @Transactional // 事务注解对速度基本无影响
    @GetMapping(value = "/effective/student/insert/batch")
    public String insertList2(int num) {
        long start = System.currentTimeMillis();
        Long maxId = studentService.getMaxId();
        long tempMaxId = (maxId == null) ? 0 : studentService.getMaxId() + 1;

        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long pId = tempMaxId + i;
            Student student = getStudent(pId + "");
            studentList.add(student);
        }
        logger.info("Mybatis SQL return :" + studentService.insertListBatch(studentList));
        long end = System.currentTimeMillis();
        logger.info("批量插入 执行时间:" + (end - start));
        return "ok";
    }


    /**
     * 循环单次单条插入-有事务 Spring事务管理
     * 解析: Spring事务管理后, SqlSession只创建一次, spring 复用 Fetched SqlSession
     * 1000条: 1956,1344,1224 ms
     * 2500条: 3437,3393,3460 ms
     * 5000条: 5967,5700,5844 ms
     * 8000条: 9975,10865,9423 ms
     */
    @Transactional
    @GetMapping(value = "/effective/student/insert/fortrans")
    public String insertListTrans(int num) {
        long start = System.currentTimeMillis();

        Long maxId = studentService.getMaxId();
        long tempMaxId = (maxId == null) ? 0 : studentService.getMaxId() + 1;

        for (int i = 0; i < num; i++) {
            long pId = tempMaxId + i;
            Student student = getStudent(pId + "");
            studentService.insert(student);
        }
        long end = System.currentTimeMillis();
        logger.info("数据条数:" + num);
        logger.info("循环插入-有事务 执行时间:" + (end - start));
        return "ok";
    }

    /**
     * 单次单条insert语句,批量插入(BEGIN END) ,加不加 @Transactional 对效率影响不大
     * 解析: 只有一次 SqlSession,
     * 1000条: 819,103,102
     * 2500条: 3344,219,237
     * 5000条: 18312,652,462
     * 8000条: 48792,1570,964ms
     */
    // @Transactional // 基本无影响
    @GetMapping(value = "/effective/student/insert/beginend")
    public String insertListBeginEnd(int num) {
        long start = System.currentTimeMillis();

        Long maxId = studentService.getMaxId();
        long tempMaxId = (maxId == null) ? 0 : studentService.getMaxId() + 1;

        List<Student> studentList = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            long pId = tempMaxId + i;
            Student student = getStudent("" + pId);
            studentList.add(student);
        }
        logger.info("Mybatis SQL return :" + studentService.insertListBeginEnd(studentList));
        long end = System.currentTimeMillis();
        logger.info("单次多条insert批量插入(BEGIN END) 执行时间:" + (end - start));
        return "ok";
    }


    /**
     * 【禁止】
     * 循环单次单条插入-无Spring事务管理
     * 解析: 每次执行都会 Creating new SqlSession, 耗时极大
     * 1000条: 4493,3174,4109 ms
     * 2500条: 11391,11088,13093 ms
     */
    @GetMapping(value = "/effective/student/insert/for")
    public String insertFori(int num) {
        long start = System.currentTimeMillis();
        long maxId = studentService.getMaxId() + 1;
        for (int i = 0; i < num; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentService.insert(student);
        }
        long end = System.currentTimeMillis();
        System.out.println("循环插入-无事务 执行时间:" + (end - start));
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
