package org.daijie.jdbc.scripting;

import org.daijie.jdbc.executor.SqlExecutor;
import org.daijie.jdbc.matedata.TableMatedata;

import java.lang.reflect.Method;
import java.util.List;

/**
 * SQL分析器
 * @param <T> 具体映射对象类型
 */
public interface SqlAnalyzer<T> {

    /**
     * 生成SQL
     * @param table 表元数据
     * @param entity 映射对象
     * @param method mapper方法
     * @param wrapper 包装工具
     */
    void generatingSql(TableMatedata table, T entity, Method method, Wrapper wrapper);

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
