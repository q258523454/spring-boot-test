

package com.myjetcachekryo.service;

import com.myjetcachekryo.entity.Student;

public interface JetCacheBaseService {

    Student add(Student student);

    Student get(Long id, boolean isUseCache);

    Student getRefresh(Long id);

}
