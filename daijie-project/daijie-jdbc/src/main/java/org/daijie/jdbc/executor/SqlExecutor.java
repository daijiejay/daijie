package org.daijie.jdbc.executor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.daijie.core.util.ClassInfoUtil;
import org.daijie.jdbc.annotation.Param;
import org.daijie.jdbc.cache.CacheManage;
import org.daijie.jdbc.matedata.MultiTableMateData;
import org.daijie.jdbc.matedata.TableMateData;
import org.daijie.jdbc.matedata.TableMateDataManage;
import org.daijie.jdbc.result.BaseResult;
import org.daijie.jdbc.result.PageResult;
import org.daijie.jdbc.result.Result;
import org.daijie.jdbc.scripting.Wrapper;
import org.daijie.jdbc.scripting.*;
import org.daijie.jdbc.transaction.Transaction;
import org.daijie.jdbc.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * SQL脚本信息
     */
    private SqlScript sqlScript;
    /**
     * 预编译SQL声明
     */
    private Statement statement;
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
     * @param fileName SQL文件路径
     */
    public SqlExecutor(String fileName) {
        this.transation = TransactionManager.createTransaction();
        this.tableMatedata = null;
        this.result = null;
        this.sqlScript = new SqlFileReaderImpl(fileName);
    }

    /**
     * 构造SQL执行器
     * @param entityClass 具体关系映射对象
     * @param method mapper方法
     * @param args mapper方法参数
     */
    public SqlExecutor(Class entityClass, Method method, Object[] args) {
        log.debug("执行mapper方法：{}", method);
        this.transation = TransactionManager.createTransaction();
        MultiWrapper multiWrapper = this.getMultiWrapper(args);
        Class returnClass = method.getReturnType();
        if (method.getReturnType() == List.class || method.getReturnType() == PageResult.class) {
            returnClass = ClassInfoUtil.getSuperClassGenricType(method.getGenericReturnType());
        }
        if (multiWrapper != null) {
            this.tableMatedata = TableMateDataManage.initTable(returnClass, multiWrapper);
            this.initSqlAnalyzer(method, multiWrapper);
        } else {
            if (entityClass == returnClass || TableMateDataManage.isBaseClass(returnClass) || returnClass.isAssignableFrom(Object.class)) {
                this.tableMatedata = TableMateDataManage.initTable(entityClass, method.getReturnType(), method.getGenericReturnType());
            } else {
                this.tableMatedata = TableMateDataManage.initMultiTable(returnClass);
            }
            this.initSqlAnalyzer(method, args);
        }
        if (method.getReturnType() == PageResult.class) {
            this.result = new PageResult(method.getReturnType());
        } else {
            this.result = new BaseResult(method.getReturnType());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.transation.getConnection();
    }

    @Override
    public Object execute() throws SQLException {
        log.debug("SQL脚本：" + this.sqlScript.getSql());
        log.debug("SQL参数：" + this.sqlScript.getParams());
        Object result = null;
        try {
            if (Type.QUERY == this.sqlScript.getScriptType()) {
                result = executeQuery();
            } else if (Type.UPDATE == this.sqlScript.getScriptType()) {
                result = executeUpdate();
                commit();
            } else if (Type.BATCH == this.sqlScript.getScriptType()) {
                result = executeBatch();
            }
        } catch (Exception e) {
            log.error("SQL执行失败：" + this.sqlScript.getSql(), e);
            if (Type.UPDATE == this.sqlScript.getScriptType()) {
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
        List<String> tableNames = getTables();
        Object resultData = CacheManage.get(tableNames, this.sqlScript.getSql() + this.sqlScript.getParams());
        if (resultData != null) {
           return resultData;
        }
        if (this.result instanceof PageResult) {
            ((PageResult) this.result).pageResult(executeQuery(this.sqlScript.getCountSql()));
            if (((PageResult) this.result).getTotal() == 0) {
                return this.result;
            }
        }
        ResultSet resultSet = executeQuery(this.sqlScript.getSql());
        resultData = this.result.mappingObjectResult(resultSet, this.tableMatedata);
        resultSet.last();
        log.debug("查询条数为：{}", resultSet.getRow());
        if (!CacheManage.isChangeTable(tableNames)) {
            CacheManage.set(tableNames, this.sqlScript.getSql() + this.sqlScript.getParams(), resultData);
        }
        return resultData;
    }

    @Override
    public Object executeUpdate() throws SQLException {
        int count = executeUpdate(this.sqlScript.getSql());
        log.debug("变更条数为：{}", count);
        if (count > 0) {
            CacheManage.remove(this.tableMatedata.getName());
        }
        return true;
    }

    @Override
    public Object executeBatch() throws SQLException {
        int[] rows = executeBatch(this.sqlScript.getSql());
        for (int row : rows) {
            log.debug("变更条数为：{}", row);
        }
        return true;
    }

    private ResultSet executeQuery(String sql) throws SQLException {
        this.statement = getConnection().prepareStatement(sql);
        createParams();
        log.debug("SQL执行：" + this.statement.toString().substring(this.statement.toString().indexOf(":") + 2));
        return ((PreparedStatement) this.statement).executeQuery();
    }

    private int executeUpdate(String sql) throws SQLException {
        this.statement = getConnection().prepareStatement(sql);
        createParams();
        log.debug("SQL执行：" + this.statement.toString().substring(this.statement.toString().indexOf(":") + 2));
        return ((PreparedStatement) this.statement).executeUpdate();
    }

    private int[] executeBatch(String sql) throws SQLException {
        this.statement = getConnection().createStatement();
        for (String row : sql.split(";")) {
            this.statement.addBatch(row);
        }
        return this.statement.executeBatch();
    }

    @Override
    public void commit() throws SQLException {
        if (!isClosed && !TransactionManager.isTransaction()) {
            this.transation.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (!isClosed && !TransactionManager.isTransaction()) {
            this.transation.rollback();
        }
    }

    @Override
    public void closed() throws SQLException {
        this.isClosed = true;
        if (this.statement != null && !this.statement.isClosed()) {
            this.statement.close();
        }
        if (!TransactionManager.isTransaction()) {
            this.transation.close();
        }
    }

    /**
     * 初始化SQL分析器
     * @param method mapper方法
     * @param args mapper方法参数
     */
    private void initSqlAnalyzer(Method method, Object[] args) {
        SqlAnalyzerImpl sqlAnalyzer = new SqlAnalyzerImpl<>();
        Object entity = null;
        Wrapper wrapper = null;
        try {
            if (args == null || args.length == 0) {
                entity = this.tableMatedata.getEntityClass().newInstance();
            } else {
                Map<String, Object> params = Maps.newHashMap();
                Annotation[][] annotations = method.getParameterAnnotations();
                for (int i = 0; i < annotations.length; i++) {
                    Object param = annotations[i];
                    Annotation[] paramAnn = annotations[i];
                    if(param == null || paramAnn.length == 0){
                        continue;
                    }
                    for (Annotation annotation : paramAnn) {
                        if(annotation.annotationType().equals(Param.class)){
                            params.put(((Param) annotation).value(), args[i]);
                        }
                    }
                }
                if (params.size() > 0 && params.size() != args.length) {
                    throw new SQLException("缺少定义参数名@Param");
                }
                if (params.size() > 0) {
                    entity = params;
                } else {
                    for (Object obj : args) {
                        if (this.tableMatedata.getEntityClass().isAssignableFrom(Object.class)) {

                        } else if (obj.getClass() == this.tableMatedata.getEntityClass()) {
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
            }
            sqlAnalyzer.generatingSql(this.tableMatedata, entity, method, wrapper);
            this.sqlScript = sqlAnalyzer;
        } catch (Exception e) {
            log.error("参数错误：", e);
        }
    }

    /**
     * 初始化多表关联查询SQL分析器
     * @param method mapper方法
     * @param multiWrapper 多表查询包装类
     */
    private void initSqlAnalyzer(Method method, MultiWrapper multiWrapper) {
        SqlAnalyzerImpl sqlAnalyzer = new SqlAnalyzerImpl<>();
        sqlAnalyzer.generatingSql((MultiTableMateData) this.tableMatedata, method, multiWrapper);
        this.sqlScript = sqlAnalyzer;
    }

    /**
     * 获取方法参数中MultiWrapper类的值
     * @param args
     * @return
     */
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

    /**
     * JDBC SQL语句占位符对应的参数构建
     * @throws SQLException
     */
    private void createParams() throws SQLException {
        if (this.statement instanceof PreparedStatement) {
            log.debug("SQL参数：" + this.sqlScript.getParams().toString());
            int index = 1;
            for (Object param : this.sqlScript.getParams()) {
                ((PreparedStatement) this.statement).setObject(index++, param);
            }
        }
    }

    /**
     * 解析获取SQL所有表名
     * @return
     */
    private List<String> getTables() {
        List<String> tables = Lists.newArrayList();
        List<SQLStatement> stmtList = SQLUtils.parseStatements(this.sqlScript.getSql(), JdbcConstants.MYSQL);
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
            tables.addAll(visitor.getTables().keySet().stream().map(name -> name.getName()).collect(Collectors.toList()));
        }
        return tables;
    }

    /**
     * JDBC SQL执行类型
     */
    public enum Type {
        QUERY,
        UPDATE,
        BATCH
    }
}
