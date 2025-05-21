package com.sharding5multidb.config.shardingsphere;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import lombok.Getter;

import org.apache.shardingsphere.infra.database.DefaultDatabase;
import org.apache.shardingsphere.infra.datasource.pool.creator.DataSourcePoolCreator;
import org.apache.shardingsphere.infra.datasource.props.DataSourceProperties;
import org.apache.shardingsphere.infra.util.expr.InlineExpressionParser;
import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.apache.shardingsphere.spring.boot.datasource.DataSourceMapSetter;
import org.apache.shardingsphere.spring.boot.exception.DataSourceJndiNotFoundServerException;
import org.apache.shardingsphere.spring.boot.util.PropertyUtil;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;


@Getter
@Configuration
public class MyShardingDataSourceProps implements EnvironmentAware {

    private static final String PREFIX = "my.shardingsphere.datasource.";

    private static final String DATA_SOURCE_NAME = "name";

    private static final String DATA_SOURCE_NAMES = "names";

    private static final String DATA_SOURCE_TYPE = "type";

    public static final String LOGIC_NAME = "logic_db";

    private static final String DATABASE_NAME_KEY = "my.shardingsphere.database.name";

    private static final String SCHEMA_NAME_KEY = "my.shardingsphere.schema.name";

    private String databaseName;

    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

    /**
     * 数据库分片配置,参考:
     * {@link ShardingSphereAutoConfiguration#setEnvironment(Environment)}
     * {@link DataSourceMapSetter}
     */
    @Override
    public void setEnvironment(Environment environment) {
        dataSourceMap.putAll(getDataSourceMap(environment));
        databaseName = getDatabaseName(environment);
    }

    public static Map<String, DataSource> getDataSourceMap(final Environment environment) {
        Map<String, DataSource> result = new LinkedHashMap<>();
        for (String each : getDataSourceNames(environment)) {
            try {
                result.put(each, getDataSource(environment, each));
            } catch (final NamingException ex) {
                throw new DataSourceJndiNotFoundServerException(ex);
            }
        }
        return result;
    }

    private static List<String> getDataSourceNames(final Environment environment) {
        StandardEnvironment standardEnv = (StandardEnvironment) environment;
        standardEnv.setIgnoreUnresolvableNestedPlaceholders(true);
        String dataSourceNames = standardEnv.getProperty(PREFIX + DATA_SOURCE_NAME);
        if (Strings.isNullOrEmpty(dataSourceNames)) {
            dataSourceNames = standardEnv.getProperty(PREFIX + DATA_SOURCE_NAMES);
        }
        return new InlineExpressionParser(dataSourceNames).splitAndEvaluate();
    }

    @SuppressWarnings("unchecked")
    private static DataSource getDataSource(final Environment environment, final String dataSourceName) throws NamingException {
        Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, String.join("", PREFIX, dataSourceName), Map.class);
        Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource [%s] properties.", dataSourceName);
        return DataSourcePoolCreator.create(new DataSourceProperties(dataSourceProps.get(DATA_SOURCE_TYPE).toString(), PropertyUtil.getCamelCaseKeys(dataSourceProps)));
    }

    public static String getDatabaseName(final Environment environment) {
        StandardEnvironment standardEnv = (StandardEnvironment) environment;
        String databaseName = standardEnv.getProperty(DATABASE_NAME_KEY);
        if (!Strings.isNullOrEmpty(databaseName)) {
            return databaseName;
        }
        String schemaName = standardEnv.getProperty(SCHEMA_NAME_KEY);
        return Strings.isNullOrEmpty(schemaName) ? DefaultDatabase.LOGIC_NAME : schemaName;
    }
}
