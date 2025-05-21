package com.inter.config.intercept;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.inter.uti.ScopeThreadUtils;
import com.inter.uti.ScopeConstants;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.io.StringReader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),})
@Slf4j
public class MybatisIntercept_DataScope implements Interceptor {
    CCJSqlParserManager ccjSqlParserManager = new CCJSqlParserManager();

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";


    /**
     * 注意: 分页查询的时候,如果是mybatis-plus内置的分页,该接口调用一次;
     * 但是如果是 com.github.Pagehelper 分页查询,count 和 page 各调用了一次
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object intercept(Invocation invocation) throws Throwable {
        String dataScope = ScopeThreadUtils.getDataScope();

        // 没有数据权限标识,无需做增强处理,直接跳过.
        if (!ScopeConstants.DATA_SCOPE.equals(dataScope)) {
            return invocation.proceed();
        }

        try {
            Object[] args = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            if (args.length == 4) {
                // 4个参数, 创建成6个参数, query(MappedStatement, Object, RowBounds, ResultHandler)
                boundSql = mappedStatement.getBoundSql(parameter);
                // 创建 CacheKey
                cacheKey = executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
            } else {
                // 6个参数: query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
            String improvedSql = "";
            // SQL 处理
            String sql = boundSql.getSql();
            // 替换空格、换行、tab缩进等
            // sql = sql.replaceAll("\\s+", " ");
            log.info("原始 SQL： {}", sql);
            // 增强sql
            Select select = (Select) ccjSqlParserManager.parse(new StringReader(sql));
            SelectBody selectBody = select.getSelectBody();
            if (selectBody instanceof PlainSelect) {
                this.setWhere((PlainSelect) selectBody);
            } else if (selectBody instanceof SetOperationList) {
                SetOperationList setOperationList = (SetOperationList) selectBody;
                List<SelectBody> selectBodyList = setOperationList.getSelects();
                selectBodyList.forEach((s) -> {
                    this.setWhere((PlainSelect) s);
                });
            }
            improvedSql = select.toString();
            log.info("增强SQL： {}", improvedSql);
            BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), improvedSql,
                    boundSql.getParameterMappings(), parameter);

            return executor.query(mappedStatement, parameter, rowBounds, (ResultHandler) args[3], cacheKey, newBoundSql);
        } finally {
            // 清除线程中权限参数
            ScopeThreadUtils.clearDataScope();
        }
    }

    /**
     * 判断是否使用 github.PageHelper 分页,并且执行了 count(), 这种情况拦截器会执行两次
     * 因此要保存增强SQL, 让第二次 Query 查询的时候也用增强SQL,
     */
    private boolean isUsePageHelperAndNeedCount() {
        // 存在 PageHelper 分页, count 和 page 各调用了一次, 因此第一次 count 先不执行 ScopeThreadUtils.cleanDataScope()
        Page<Object> localPage = PageHelper.getLocalPage();
        // PageHelper分页 并且 计算 count()
        return null != localPage && localPage.isCount();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    protected void setWhere(PlainSelect plainSelect) {
        Expression sqlSegment = enhanceSql(plainSelect.getWhere());
        if (null != sqlSegment) {
            plainSelect.setWhere(sqlSegment);
        }
    }

    @SneakyThrows
    public Expression enhanceSql(Expression where) {
        StringBuilder sqlString = new StringBuilder();
        sqlString.append(" AND id >= 0");
        if (StringUtils.isNotBlank(sqlString.toString())) {
            if (where == null) {
                where = new HexValue(" 1 = 1 ");
            }
            return CCJSqlParserUtil.parseCondExpression(where + sqlString.toString());
        } else {
            return where;
        }
    }
}

