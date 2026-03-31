package shiro2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import shiro2.pojo.entity.ExamUser;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamUserMapper extends BaseMapper<ExamUser> {
    int insert(ExamUser record);

    List<ExamUser> selectByUsername(@Param("username") String username);
}