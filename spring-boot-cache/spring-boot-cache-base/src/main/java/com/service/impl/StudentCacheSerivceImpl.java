package com.service.impl;

import com.dao.StudentMapper;
import com.entity.Student;
import com.service.StudentCacheSerivce;
import com.service.StudentSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@Service
public class StudentCacheSerivceImpl implements StudentCacheSerivce {

    @Autowired
    private StudentMapper mapper;


    /**
     * 查
     * 首先在缓存中查找,如果没有, 则执行方法并缓存结果，然后返回数据
     * 默认 key:方法参数 , 默认 value:方法返回值
     * 下面的 key是自定义id, #id对应参数id, #{参数名}
     * 运行流程:
     *   1.方法运行之前，先去查询Cache（缓存组件），按照cacheNames指定的名字获取；
     *    （CacheManager先获取相应的缓存），第一次获取缓存如果没有Cache组件会自动创建。
     *
     *   2.去Cache中查找缓存的内容，使用一个key，默认就是方法的参数值；
     *      key是按照某种策略生成的；默认是使用keyGenerator生成的，
     * 　　  Spring默认加载的是SimpleCacheManage，SimpleKeyGenerator生成key的默认策略是：
     *                        如果没有参数；key=new SimpleKey()
     *                        如果有一个参数：key=参数的值
     *                        如果有多个参数：key=new SimpleKey(params)
     *
     *   3.没有查到缓存就调用目标方法；
     *   4.将目标方法返回的结果，放进缓存中
     */
    @Override
    @Cacheable(cacheNames = "myCache", key = "'myId_'+#id")
    public Student selectStudentById(Integer id) {
        return mapper.selectStudentById(id);
    }


    /**
     * 删
     */
    @Override
    @CacheEvict(cacheNames = "myCache", key = "'myId_'+#id")
    public Integer deleteStudentById(Integer id) {
        return mapper.deleteStudentById(id);
    }


    /**
     * 改
     * 先执行方法，然后将返回值放回缓存。可以用作缓存的更新
     * @CachePut注解的作用简单的说一句话：既调用方法，又缓存数据。
     * @cachePut和@Cacheable两个注解都可以用于填充缓存，但使用上略有点差异，
     * @Cacheable注解的执行流程是先在按key在缓存中查找，存在则返回，不存在则执行目标方法，并缓存目标方法的结果。
     * 而@CachePut并不会检查缓存，总是先执行目标方法，并将目标方法的结果保存到缓存中。
     * 实际中比如执行到更新操作时，则希望将最新的数据更新到缓存，如果该方法返回异常，将不再执行保存缓存的逻辑。
     */
    @Override
    @CachePut(cacheNames = "myCache", key = "'myId_'+#student.id")
    public Student updateStudent(Student student) {
        Integer res = mapper.updateStudent(student);
        if (null != res) {
            return student;
        } else {
            return null;
        }
    }


    @Override
    public Integer insertStudent(Student student) {
        return mapper.insertStudent(student);
    }

}
