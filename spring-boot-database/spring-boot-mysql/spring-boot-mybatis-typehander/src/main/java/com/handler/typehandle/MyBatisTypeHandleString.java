package com.handler.typehandle;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * TypeHandler 有两种激活方式：
 * 方式一:  在xml中指定激活,需要用的时候就手动指定
 *     ...
 *     <result column="name" jdbcType="VARCHAR" property="name" typeHandler="com.handler.typehandle.MyBatisTypeHandleString"/>
 *     ...
 *     values (
 *         #{name,jdbcType=VARCHAR,typeHandler=com.handler.typehandle.MyBatisTypeHandleString},
 *       )
 * 方式二: 配置中统一激活, xml中无需指定, 自动匹配 字段类型关系后执行
 *     type-handlers-package: com.handler.typehandle
 *     这样配置后, 所有的mybatis都会按照类型自动执行,
 *     例如:
 *     Typehander写成 @MappedTypes({String.class}), @MappedJdbcTypes({JdbcType.CHAR}) BaseTypeHandler<String>
 *     那么 xml中 在 数据库字段类型为char<---->Java字段类型为String 之间自定执行，无需指定
 *
 *描述:
 *     1.@MappedJdbcTypes定义的是JdbcType类型，这里的类型不可自己随意定义，必须要是枚举类org.apache.ibatis.type.JdbcType所枚举的数据类型。
 *     2.@MappedTypes定义的是JavaType的数据类型，描述了哪些Java类型可被拦截。
 *     3.在我们启用了我们自定义的这个TypeHandler之后，数据的读写都会被这个类所过滤
 *     4.在setNonNullParameter方法中，我们重新定义要写往数据库的数据。
 *     5.在另外三个方法中我们将从数据库读出的数据类型进行转换。
 */
@MappedTypes({String.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class MyBatisTypeHandleString extends BaseTypeHandler<String> {

    // 插入:数据转换后插入数据库
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String str, JdbcType jdbcType) throws SQLException {
        String insertSql = str + "_插入";
        preparedStatement.setString(i, insertSql);
    }

    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String strFromSQL = rs.getString(columnName);
        String str = null;
        str = "查询_" + strFromSQL;
        return str;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String strFromSQL = rs.getString(columnIndex);
        String str = null;
        str = "查询_" + strFromSQL;
        return str;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String strFromSQL = cs.getString(columnIndex);
        String str = null;
        str = "查询_" + strFromSQL;
        return str;
    }
}
