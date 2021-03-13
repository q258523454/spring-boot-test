package com.batch.aspects;


import com.alibaba.fastjson.JSON;
import com.batch.controller.OracleControllerUpdate_Batch_AffectRows_Aop_block;
import com.batch.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @date 2020-07-01 10:18
 * @modify
 */
@Slf4j
@Aspect
@Component
public class MybatisUpdateSubBatchTransSelfAspect {

    @Pointcut("@annotation(com.batch.aspects.MybatisUpdateSubBatchTransSelf)")
    public void batchPointCut() {
    }


    @Before("batchPointCut()")
    public void before(JoinPoint joinPoint) {
        log.info("aop @Before");
    }


    /**
     * 问题：嵌套事务、更新同一条数据会引起AOP切面阻塞{@link OracleControllerUpdate_Batch_AffectRows_Aop_block}
     * 注意要点:
     * 1.Batch操作本身是有原子性的, 要么全部成功要么全部失败,这里由于Oracle限制条数,将Batch进行了划分
     * 一个Batch分成了多个小的Batch,因此就必须开启事务来解决原子性,由于是AOP的原因,无法声明注解.必须手动开启事务。
     * 2.Batch需要返回成功的条数,必须用1个变量来保存循环执行的SQL返回
     */
    @SuppressWarnings(value = {"rawtypes"})
    @Around(value = "batchPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        // AOP事务需要用编程式,否则不会生效
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

        log.info("@Around start");
        Object[] args = joinPoint.getArgs();
        // Batch操作的输入参数只允许为一个
        if (null == args || args.length != 1) {
            throw new RuntimeException("batch操作接口输入参数必须有且仅有一个入参");
        }

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Class returnType = methodSignature.getReturnType();
        // 确保返回类型为整数
        if (returnType != Integer.class && returnType != int.class) {
            throw new RuntimeException("batch操作接口返回参数必须是整数类型");
        }
        // 只需要获取接口方法上的第一个入参
        Object arg0 = args[0];
        List<Object> batchList = new ArrayList<>();
        List<Object> elementList = null;

        // 获取方法上的注解
        Method method = methodSignature.getMethod();
        MybatisUpdateSubBatchTransSelf annotation = method.getAnnotation(MybatisUpdateSubBatchTransSelf.class);
        // 通过注解获取设定的一次执行SQL的条数
        int subBatchNum = annotation.num();

        // 统计成功执行的SQL数目
        int successNum = 0;
        Object proceed = null;
        if (!(arg0 instanceof List)) {
            throw new RuntimeException("batch操作接口输入参数只允许为List类型");
        }

        List list = (List) arg0;
        List subList = null;
        int listSize = list.size();
        // list分批次数
        int circleNum = (int) Math.ceil(1.0 * listSize / subBatchNum);
        try {
            // 分批执行批量SQL
            for (int i = 0; i < circleNum; i++) {
                int leftIndex = (i * subBatchNum);
                // subBatchNum * (i + 1) 有可能超过实际元素长度, 取最小值
                int rightIndex = Math.min(subBatchNum * (i + 1), list.size());
                subList = list.subList(leftIndex, rightIndex);
                // 注意proceed的参数传递形式
                proceed = joinPoint.proceed(new Object[]{subList});
                successNum = successNum + (int) proceed;
            }
        } catch (Throwable throwable) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            log.error("@Around 出现异常:{}", throwable.getMessage());
            log.error("错误数据:{}", JSON.toJSONString(subList));
            throw new RuntimeException("@Around 出现异常:" + throwable.getMessage());
        }
        wPlatformTransactionManager.commit(wTransactionStatus);
        log.info("@Around end");
        return successNum;
    }

    @After(value = "batchPointCut()")
    public void after(JoinPoint joinPoint) {
        log.info("@After");
    }

    @AfterReturning(pointcut = "batchPointCut()", returning = "result")
    public void afterreturning(JoinPoint joinPoint, Object result) {
        log.info("@AfterReturning:" + JSON.toJSONString(result));
    }


    /**
     * 只有连接点抛出异常的时候才执行 {@link AfterThrowing}
     * @param joinPoint  连接点
     * @param ex 这里定义为 Exception, 表示通知所有异常.可以指定某个异常
     */
    @AfterThrowing(pointcut = "batchPointCut()", throwing = "ex")
    public void afterthrowing(JoinPoint joinPoint, Exception ex) {
        log.info("@AfterThrowing");
    }


}
