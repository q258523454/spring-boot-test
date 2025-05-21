package com.shardinghint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shardinghint.entity.Student;

/**
 * 继承 IService 是加强版的 CRUD, 针对的是 Service 层, 含有 batch 操作.
 */
public interface StudentService extends IService<Student> {
}
