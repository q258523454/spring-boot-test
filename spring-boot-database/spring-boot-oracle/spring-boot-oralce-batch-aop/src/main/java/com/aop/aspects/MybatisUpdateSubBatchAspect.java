package com.aop.aspects;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @date 2020-07-01 10:18
 * @modify
 */
@Slf4j
@Aspect
@Component
public class MybatisUpdateSubBatchAspect {

    @Pointcut("@annotation(com.aop.aspects.MybatisUpdateSubBatch)")
    public void batchPointCut() {
    }


    @Before("batchPointCut()")
    public void before(JoinPoint joinPoint) {
        log.info("aop @Before");
    }

    @Around(value = "batchPointCut()")
    @SuppressWarnings(value = "rawtypes")
    public Object around(ProceedingJoinPoint joinPoint) {
        log.info("@Around start");
        int length = joinPoint.getArgs().length;
        if (length != 1) {
            throw new RuntimeException("batch操作接口输入参数必须有且仅有一个入参");
        }
        Signature signature = joinPoint.getSignature();
        Class returnType = ((MethodSignature) signature).getReturnType();
        if (returnType != Integer.class && returnType != int.class) {
            throw new RuntimeException("batch操作接口返回参数必须是整数类型");
        }
        // 只获取接口方法上的第一个入参
        Object arg1 = joinPoint.getArgs()[0];

        Object proceed = null;
        // 统计成功执行的SQL数目
        int successNum = 0;
        if (!(arg1 instanceof List)) {
            throw new RuntimeException("batch操作接口输入参数只允许为List类型");
        }
        List list = (List) arg1;
        int listSize = list.size();

        // 每次循环执行SQL的条数 (可直接获取注解中的num(),这里测试直接定义为5)
        final int subBatchNum = 5;
        // list分批循环次数
        int circleNum = (int) Math.ceil(1.0 * listSize / subBatchNum);
        List subList = null;
        try {
            // 分批执行批量SQL
            for (int i = 0; i < circleNum; i++) {
                int leftIndex = (i * subBatchNum);
                // subBatchNum * (i + 1) 有可能超过实际元素长度, 取最小值
                int righIndex = Math.min(subBatchNum * (i + 1), list.size());
                subList = list.subList(leftIndex, righIndex);
                // 注意proceed的参数传递形式
                proceed = joinPoint.proceed(new Object[]{subList});
                successNum = successNum + (int) proceed;
            }
        } catch (Throwable throwable) {
            log.error("@Around 出现异常:{}", throwable.getMessage());
            log.error("错误数据:{}", JSON.toJSONString(subList));
            throw new RuntimeException("@Around 出现异常:" + throwable.getMessage());
        }

        log.info("@Around end");
        return successNum;
    }

    @After(value = "batchPointCut()")
    public void after(JoinPoint joinPoint) {
        log.info("aop-@After");
    }

    @AfterReturning(pointcut = "batchPointCut()", returning = "result")
    public void afterreturning(JoinPoint joinPoint, Object result) {
        log.info("aop-@AfterReturning:" + JSON.toJSONString(result));
    }


    /**
     * 只有连接点抛出异常的时候才执行 {@link org.aspectj.lang.annotation.AfterThrowing}
     * @param joinPoint  连接点
     * @param ex 这里定义为 Exception, 表示通知所有异常.可以指定某个异常
     */
    @AfterThrowing(pointcut = "batchPointCut()", throwing = "ex")
    public void afterthrowing(JoinPoint joinPoint, Exception ex) {
        log.info("@AfterThrowing");
    }


}
