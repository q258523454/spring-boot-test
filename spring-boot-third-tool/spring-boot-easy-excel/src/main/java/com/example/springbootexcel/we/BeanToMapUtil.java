
package com.example.springbootexcel.we;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * bean2map工具类
 */
@Slf4j
public enum BeanToMapUtil {
    ;

    /**
     * bean转map,避免 setAccessible(true)
     *
     * @param obj obj
     * @return Map
     * @throws IntrospectionException    IntrospectionException
     * @throws InvocationTargetException InvocationTargetException
     * @throws IllegalAccessException    IllegalAccessException
     */
    public static Map<String, Object> beanToMap(Object obj) throws IntrospectionException,
            InvocationTargetException, IllegalAccessException {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = getBeanInfo(obj);
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptor(beanInfo);
        return getMap(obj, propertyDescriptors);
    }

    /**
     * getBeanInfo
     *
     * @param object object
     * @return BeanInfo
     * @throws IntrospectionException IntrospectionException
     */
    private static BeanInfo getBeanInfo(Object object) throws IntrospectionException {
        return Introspector.getBeanInfo(object.getClass());
    }

    /**
     * getPropertyDescriptor
     *
     * @param beanInfo beanInfo
     * @return PropertyDescriptor[]
     */
    private static PropertyDescriptor[] getPropertyDescriptor(BeanInfo beanInfo) {
        return beanInfo.getPropertyDescriptors();
    }

    /**
     * getMap
     *
     * @param object              object
     * @param propertyDescriptors propertyDescriptors
     * @return Map
     * @throws InvocationTargetException InvocationTargetException
     * @throws IllegalAccessException IllegalAccessException
     */
    private static Map<String, Object> getMap(Object object, PropertyDescriptor[] propertyDescriptors)
            throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(object);
                map.put(key, value);
            }
        }
        return map;
    }
}
