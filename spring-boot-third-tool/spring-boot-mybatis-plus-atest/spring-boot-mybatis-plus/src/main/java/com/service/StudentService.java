package com.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.StudentPlusMapper;
import com.entity.Student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;


/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得 CRUD 功能
 * 继承 BaseMapper 是基本的 CRUD, 针对的是 DAO 层.
 * 继承 IService 是加强版的 CRUD, 针对的是 Service 层, 含有更丰富 batch 操作.
 * 二者结合使用
 * 多表操作或复杂SQL, 还是需要去写 mapper.xml
 */
@Service
public class StudentService extends ServiceImpl<StudentPlusMapper, Student> {

    public List<Student> selectList(LambdaQueryWrapper<Student> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    /**
     * selective 查询
     */
    public List<Student> selectList(Student entity) {
        LambdaQueryWrapper<Student> selectiveWrapper = getSelectiveWrapper(entity);
        return selectList(selectiveWrapper);
    }

    /**
     * selective wrapper
     */
    public LambdaQueryWrapper<Student> getSelectiveWrapper(Student entity) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!StringUtils.isEmpty(entity.getId()), Student::getId, entity.getId());
        wrapper.eq(!StringUtils.isEmpty(entity.getName()), Student::getName, entity.getName());
        wrapper.eq(!StringUtils.isEmpty(entity.getAge()), Student::getAge, entity.getAge());
        return wrapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(List<Student> entityList) {
        return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
    }
}
