
package com.myjetcache.service;

import com.myjetcache.entity.Student;

public interface RefreshService {

    String getRefreshStringCache(String key);

    Student getRefreshMapCache(Object obj);
}
