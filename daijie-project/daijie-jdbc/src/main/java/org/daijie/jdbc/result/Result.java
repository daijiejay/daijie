package org.daijie.jdbc.result;

import org.daijie.jdbc.matedata.TableMatedata;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询结果对象映射
 */
public interface Result {

    /**
     * 查询结果映射对象
     * @param resultSet JDBC执行查询结果对象
     * @param tableMatedata 表元数据
     * @return Objcet 返回表元数据中对应的对象
     * @throws SQLException SQL异常
     */
    Object mappingObjectResult(ResultSet resultSet, TableMatedata tableMatedata) throws SQLException;

    /**
     * 将已映射对象结果集跟据返回对象类型对应处理
     * @param result 已映射对象结果集
     * @return Objcet 返回表元数据中对应的对象
     * @throws SQLException SQL异常
     */
    Object getResult(Object result) throws SQLException;

    /**
     * 是否多表关联查询
     * @return 返回布尔值
     */
    boolean isMulti();
}
