package com.redis.service.impl;

import com.redis.dao.StudentMapper;
import com.redis.entity.Student;
import com.redis.redisUtil.RedisCacheUtil;
import com.redis.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created By
 *
 * @date :   2018-08-28
 */

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> selectAllUserPro() {
        String cache_key = RedisCacheUtil.CAHCE_NAME + "|getUserListPro";
        // 先去缓存中取
        List<Student> result_cache = RedisCacheUtil.getListCachePro(cache_key, Student.class);
        if (result_cache == null) {
            // 缓存中没有再去数据库取，并插入缓存（缓存时间为5秒）
            result_cache = studentMapper.selectAllStuddent();
            RedisCacheUtil.putListCacheProWithExpireTime(cache_key, result_cache, RedisCacheUtil.CAHCE_TIME);
            log.warn("缓存不存在,添加. put cache with key:{}", cache_key);
        } else {
            log.warn("缓存已存在,查询. get cache with key:{}", cache_key);
        }
        return result_cache;
    }
}
