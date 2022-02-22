package com.mysql.affectrows.dao;

import com.mysql.affectrows.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    /**
     * @mbg.generated 2019-12-17
     */
    int insert(Student record);

    Long getMaxId();

    /**
     * @mbg.generated 2019-12-17
     */
    Student selectByPrimaryKey(Integer id);

    List<Student> selectAll();

    int selectMaxId();

    int updateByName(@Param("updated") Student updated, @Param("name") String name);

    List<Student> selectByName(@Param("name") String name);

    /**
     * 批量插入——单条SQL foreach
     */
    int insertListBatch(@Param("studentList") List<Student> studentList);

    // 单条件 update batch
    int updateListByIdBatch(@Param("studentList") List<Student> studentList);

    // 多条件 update batch (这里为了方便模拟,两个条件都是id,实际情况换成不同条件即可)
    int updateListByIdAndIdBatch(@Param("studentList") List<Student> studentList);

    int deleteByIdList(@Param("list") List<String> list);


}