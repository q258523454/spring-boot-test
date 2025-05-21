
package com.inter.config.intercept;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


/**
 * 拦截SQL执行，打印SQL日志
 */
@Intercepts({
        /*
         * 经常用到的就只有update和两个query
         *
         * @Signature 参数说明
         * - type: 指定代理对象
         * - method: 代理对象中的方法
         * - args: 代理对象中的方法有哪些参数
         */
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class})
})
@Slf4j
public class MybatisIntercept_SqlLog implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            // 获得查询方法的参数，比如selectById(Integer id,String name)，那么就可以获取到四个参数分别是：
            //{id:1,name:"user1",param1:1,param2:"user1"}
            parameter = invocation.getArgs()[1];
        }
        // 获得mybatis的*mapper.xml文件中映射的方法，如：com.best.dao.UserMapper.selectById
        String sqlId = mappedStatement.getId();

        // 将参数和映射文件组合在一起得到BoundSql对象
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // 获取配置信息
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue = null;

        log.info("---------------SQL日志拦截器:" + sqlId + "---------------");
        // 通过配置信息和BoundSql对象来生成带值得sql语句
        String sql = getSql(configuration, boundSql);
        // 打印sql语句
        log.info("==> sql:" + sql);
        // 先记录执行sql语句前的时间
        long start = System.currentTimeMillis();
        try {
            // 开始执行sql语句
            returnValue = invocation.proceed();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // 记录执行sql语句后的时间
        long end = System.currentTimeMillis();
        // 得到执行sql语句的用了多长时间
        long time = (end - start);
        // 以毫秒为单位打印
        log.info("<== sql执行历时：" + time + "毫秒");
        // 返回值，如果是多条记录，那么此对象是一个list，如果是一个bean对象，那么此处就是一个对象，也有可能是一个map
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // do nothing
    }

    public static String getSql(Configuration configuration, BoundSql boundSql) {
        // 获得参数对象
        Object parameterObject = boundSql.getParameterObject();
        // 获得映射的对象参数
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 替换空格、换行、tab缩进等
        String sql = boundSql.getSql().replaceAll("\\s+", " ");
        // 如果参数个数大于0且参数对象不为空，说明该sql语句是带有条件的
        if (parameterMappings.size() > 0 && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果SQL条件只有一个参数，且属于typeHandlerRegistry,直接替换即可
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                // 如果是一个参数则只替换一次，将问号直接替换成值
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                // 将映射文件的参数和对应的值返回。
                // MetaObject主要是封装了 originalObject对象，提供了 get和 set的方法用于获取和设置 originalObject的属性值
                // 主要支持对 JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                // 遍历参数，如:id,name等
                for (ParameterMapping parameterMapping : parameterMappings) {
                    // 获得属性名，如id,name等字符串
                    String propertyName = parameterMapping.getProperty();
                    // 检查该属性是否在metaObject中
                    if (metaObject.hasGetter(propertyName)) {
                        // 如果在metaObject中，那么直接获取对应的值
                        Object obj = metaObject.getValue(propertyName);
                        // 然后将问号?替换成对应的值。
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        // 最后将sql语句返回
        return sql;
    }

    /**
     * 如果是字符串对象则加上单引号返回，如果是日期则也需要转换成字符串形式，如果是其他则直接转换成字符串返回。
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }
}
