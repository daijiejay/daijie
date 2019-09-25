package org.daijie.jdbc.executor;

import org.daijie.core.util.ClassInfoUtil;
import org.daijie.jdbc.cache.CacheManage;
import org.daijie.jdbc.matedata.MultiTableMateData;
import org.daijie.jdbc.matedata.TableMateData;
import org.daijie.jdbc.matedata.TableMateDataManage;
import org.daijie.jdbc.result.BaseResult;
import org.daijie.jdbc.result.PageResult;
import org.daijie.jdbc.result.Result;
import org.daijie.jdbc.scripting.MultiWrapper;
import org.daijie.jdbc.scripting.SqlAnalyzer;
import org.daijie.jdbc.scripting.SqlAnalyzerImpl;
import org.daijie.jdbc.scripting.Wrapper;
import org.daijie.jdbc.transaction.Transaction;
import org.daijie.jdbc.transaction.TransactionManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * SQL执行器的具体实现
 * @author daijie
 * @since 2019/5/23
 */
public class SqlExecutor implements Executor {
    private final Logger log = LoggerFactory.getLogger(SqlExecutor.class);
    /**
     * 是否关闭事务
     */
    private boolean isClosed = false;
    /**
     * 表元数据
     */
    private final TableMateData tableMatedata;
    /**
     * SQL分析器
     */
    private SqlAnalyzer sqlAnalyzer;
    /**
     * 预编译SQL声明
     */
    private PreparedStatement statement;
    /**
     *  事务管理
     */
    private final Transaction transation;
    /**
     * 查询结果对象映射
     */
    private final Result result;

    /**
     * 构造SQL执行器
     * @param entityClass 具体关系映射对象
     * @param method mapper方法
     * @param args mapper方法参数
     */
    public SqlExecutor(Class entityClass, Method method, Object[] args) {
        log.debug("执行mapper方法：{}", method);
        this.transation = TransactionManage.createTransaction();
        MultiWrapper multiWrapper = this.getMultiWrapper(args);
        Class returnClass = method.getReturnType();
        if (method.getReturnType() == List.class || method.getReturnType() == PageResult.class) {
            returnClass = ClassInfoUtil.getSuperClassGenricType(method.getGenericReturnType());
        }
        if (multiWrapper != null) {
            this.tableMatedata = TableMateDataManage.initTable(returnClass, multiWrapper);
            this.initSqlAnalyzer(method, multiWrapper);
        } else {
            this.tableMatedata = TableMateDataManage.initTable(entityClass, method.getReturnType(), method.getGenericReturnType());
            this.initSqlAnalyzer(method, args);
        }
        if (method.getReturnType() == PageResult.class) {
            this.result = new PageResult(method.getReturnType(), multiWrapper != null);
        } else {
            this.result = new BaseResult(method.getReturnType(), multiWrapper != null);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.transation.getConnection();
    }

    @Override
    public Object execute() throws SQLException {
        log.debug(this.sqlAnalyzer.getSql());
        Object result = null;
        try {
            if (Type.QUERY == this.sqlAnalyzer.getScriptType()) {
                result = executeQuery();
            } else if (Type.UPDATE == this.sqlAnalyzer.getScriptType()) {
                result = executeUpdate();
                commit();
            }
        } catch (Exception e) {
            log.error("SQL执行失败：" + this.sqlAnalyzer.getSql(), e);
            if (Type.UPDATE == this.sqlAnalyzer.getScriptType()) {
                rollback();
            }
            throw e;
        } finally {
            closed();
        }
        return result;
    }

    @Override
    public Object executeQuery() throws SQLException {
        if (CacheManage.get(this.tableMatedata.getName(), this.sqlAnalyzer.getSql()) != null) {
           return this.result.getResult(CacheManage.get(this.tableMatedata.getName(), this.sqlAnalyzer.getSql()));
        }
        if (this.result instanceof PageResult) {
            ((PageResult) this.result).pageResult(executeQuery(this.sqlAnalyzer.getCountSql()));
            if (((PageResult) this.result).getTotal() == 0) {
                return this.result;
            }
        }
        ResultSet resultSet = executeQuery(this.sqlAnalyzer.getSql());
        Object resultData = this.result.mappingObjectResult(resultSet, this.tableMatedata);
        resultSet.last();
        log.debug("查询条数为：{}", resultSet.getRow());
        if (!CacheManage.isChangeTable(this.tableMatedata.getName())) {
            CacheManage.set(this.tableMatedata.getName(), this.sqlAnalyzer.getSql(), resultData);
        }
        return resultData;
    }

    @Override
    public Object executeUpdate() throws SQLException{
        int count = executeUpdate(this.sqlAnalyzer.getSql());
        log.debug("变更条数为：{}", count);
        if (count > 0) {
            CacheManage.remove(this.tableMatedata.getName());
        }
        return true;
    }

    private ResultSet executeQuery(String sql) throws SQLException {
        this.statement = getConnection().prepareStatement(sql);
        createParams();
        log.debug(this.statement.toString());
        return this.statement.executeQuery();
    }

    private int executeUpdate(String sql) throws SQLException {
        this.statement = getConnection().prepareStatement(sql);
        createParams();
        log.debug(this.statement.toString());
        return this.statement.executeUpdate();
    }

    @Override
    public void commit() throws SQLException {
        if (!isClosed && !TransactionManage.isTransaction()) {
            this.transation.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (!isClosed && !TransactionManage.isTransaction()) {
            this.transation.rollback();
        }
    }

    @Override
    public void closed() throws SQLException {
        this.isClosed = true;
        if (this.statement != null && !this.statement.isClosed()) {
            this.statement.close();
        }
        if (!TransactionManage.isTransaction()) {
            this.transation.close();
        }
    }

    /**
     * 初始化SQL分析器
     * @param method mapper方法
     * @param args mapper方法参数
     */
    private void initSqlAnalyzer(Method method, Object[] args) {
        this.sqlAnalyzer = new SqlAnalyzerImpl<>();
        Object entity = null;
        Wrapper wrapper = null;
        MultiWrapper multiWrapper = null;
        try {
            if (args == null || args.length == 0) {
                entity = this.tableMatedata.getEntityClass().newInstance();
            } else {
                for (Object obj : args) {
                    if (obj.getClass() == this.tableMatedata.getEntityClass()) {
                        entity = obj;
                    } else if (obj instanceof List) {
                        entity = obj;
                    } else if (obj instanceof Serializable) {
                        entity = this.tableMatedata.getEntityClass().newInstance();
                        Field field = this.tableMatedata.getPrimaryKey().getField();
                        field.setAccessible(true);
                        field.set(entity, obj);
                    } else if (obj instanceof Wrapper) {
                        wrapper = (Wrapper) obj;
                    }
                }
            }
            this.sqlAnalyzer.generatingSql(this.tableMatedata, entity, method, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化多表关联查询SQL分析器
     * @param method mapper方法
     * @param multiWrapper 多表查询包装类
     */
    private void initSqlAnalyzer(Method method, MultiWrapper multiWrapper) {
        this.sqlAnalyzer = new SqlAnalyzerImpl<>();
        this.sqlAnalyzer.generatingSql((MultiTableMateData) this.tableMatedata, method, multiWrapper);
    }

    private MultiWrapper getMultiWrapper(Object[] args) {
        if (args == null) {
            return null;
        }
        for (Object obj : args) {
            if (obj instanceof MultiWrapper) {
                return (MultiWrapper) obj;
            }
        }
        return null;
    }

    private void createParams() throws SQLException {
        log.debug(this.sqlAnalyzer.getParams().toString());
        int index = 1;
        for (Object param : this.sqlAnalyzer.getParams()) {
            this.statement.setObject(index++, param);
        };
    }

    public enum Type {
        QUERY,
        UPDATE
    }
}
