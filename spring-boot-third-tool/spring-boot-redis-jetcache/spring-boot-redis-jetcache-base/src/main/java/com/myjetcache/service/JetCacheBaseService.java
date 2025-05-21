

package com.myjetcache.service;

import com.myjetcache.entity.Student;

public interface JetCacheBaseService {

    Student add(Student student);

    void delete(Long id);

    void update(Student student);

    Student get(Long id);

    Student get(Long id, boolean isUseCache);
}
