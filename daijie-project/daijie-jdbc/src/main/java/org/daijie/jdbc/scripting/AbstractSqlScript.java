package org.daijie.jdbc.scripting;

import org.daijie.jdbc.executor.SqlExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL脚本信息基础实现
 */
public abstract class AbstractSqlScript implements SqlScript {

    /**
     * SQL脚本操作类型
     */
    protected SqlExecutor.Type type;

    /**
     * SQL语句
     */
    protected String sql;

    /**
     * 查询总数SQL语句
     */
    protected String countSql;

    /**
     * 占位符对应的参数
     */
    protected List<Object> params = new ArrayList<>();

    @Override
    public SqlExecutor.Type getScriptType() {
        return this.type;
    }

    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public String getCountSql() {
        return this.countSql;
    }

    @Override
    public List<Object> getParams() {
        return params;
    }
}
