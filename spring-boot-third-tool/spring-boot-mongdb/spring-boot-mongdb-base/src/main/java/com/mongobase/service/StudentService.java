

package com.mongobase.service;

import com.mongobase.pojo.entity.Student;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class StudentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 注意: 开启事务的情况，复制集 无法在事务下查询,必须读主库
     * 否则会报错: Read preference in a transaction must be primary
     *
     * 解决办法:
     * 1. 从主库读取
     * 2. 查询不走事务
     */
    public Long getMaxSid() {
        Criteria criteria = Criteria.where("sid").exists(true);
        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "sid")).limit(1);
        // 只需要sid
        // query.fields().include("sid");
        List<Student> studentList = mongoTemplate.find(query, Student.class);
        if (CollectionUtils.isEmpty(studentList) || studentList.size() < 1) {
            log.warn("no data");
            return 0L;
        }
        return studentList.get(0).getSid();
    }

    /**
     * 强制不走事务下的查询
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Long getMaxSidNoTrans() {
        Criteria criteria = Criteria.where("sid").exists(true);
        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "sid")).limit(1);
        // 只需要sid
        // query.fields().include("sid");
        List<Student> studentList = mongoTemplate.find(query, Student.class);
        if (CollectionUtils.isEmpty(studentList) || studentList.size() < 1) {
            log.warn("no data");
            return 0L;
        }
        return studentList.get(0).getSid();
    }

    public List<Student> selectAll() {
        return mongoTemplate.findAll(Student.class);
    }

    public void insertOne(Student student) {
        mongoTemplate.insert(student);
    }

    public void insertAll(List<Student> list) {
        mongoTemplate.insertAll(list);
    }
}
