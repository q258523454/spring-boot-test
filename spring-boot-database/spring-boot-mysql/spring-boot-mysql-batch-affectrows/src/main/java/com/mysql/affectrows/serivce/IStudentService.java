package com.mysql.affectrows.serivce;


import com.mysql.affectrows.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @date 2020-03-12 22:01
 * @modify
 */
public interface IStudentService {

    Long getMaxId();

    List<Student> selectAll();

    int selectMaxId();

    int insert(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByName(Student updated, String name);

    List<Student> selectByName(String name);

    int insertListBatch(List<Student> studentList);

    /**
     * 单条件 update batch
     */
    int updateListByIdBatch(List<Student> studentList);

    /**
     * 多条件 update batch (这里为了方便模拟,两个条件都是id,实际情况换成不同条件即可)
     */
    int updateListByIdAndIdBatch(@Param("studentList") List<Student> studentList);


    int deleteByIdList(List<String> list);
}
