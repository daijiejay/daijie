package org.daijie.jdbc.scripting;

import org.daijie.jdbc.executor.SqlExecutor;

import java.util.List;

/**
 * SQL脚本信息
 */
public interface SqlScript {

    /**
     * 获取数据库脚本操作类型
     * @return Type 数据库操作类型
     */
    SqlExecutor.Type getScriptType();

    /**
     * 获取SQL语句
     * @return String SQL语句
     */
    String getSql();

    /**
     * 获取查询总数SQL语句
     * @return String SQL语句
     */
    String getCountSql();

    /**
     * 获取点位符对应的参数
     * @return Lst 点位符对应的参数
     */
    List<Object> getParams();
}
