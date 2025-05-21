package com.myjetcache.service;

import com.myjetcache.entity.Student;

import java.util.List;


public interface IOracleStudentService {
    List<Student> selectAll();

    int insert(Student record);

    long getMaxId();

    /**
     * 批量插入——单条SQL batch
     */
    int insertListBatch(List<Student> studentList);

    int update(Student record);

    int updateRequire(Student record);

    int updateRequireNew(Student record);

    int updateByName(Student updated, String name);

    /**
     * 批量更新——单条SQL batch
     */
    public int updateListByIdBatchOrg(List<Student> studentList);

    /**
     * 批量更新——AOP分片Batch, 单条SQL batch变成了多个分片Batch
     */
    public int updateListByIdBatchAop(List<Student> studentList);

    /**
     * 注解声明式事务 —— 对AOP不生效
     */
    public int updateListByIdBatchTransAop(List<Student> studentList);

    public int updateListByIdBatchAopHaveTrans(List<Student> studentList);

    public int updateListByIdBatchAopHaveTransSelf(List<Student> studentList);

    public int updateListByIdBatchTransAopHaveTransSelf(List<Student> studentList);

    public int updateListByIdBatchTransCatchExceptionAop(List<Student> studentList);
}
