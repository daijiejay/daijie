package org.daijie.jdbc.scripting;

import cn.hutool.core.bean.BeanUtil;
import org.daijie.jdbc.annotation.Delete;
import org.daijie.jdbc.annotation.Insert;
import org.daijie.jdbc.annotation.Select;
import org.daijie.jdbc.annotation.Update;
import org.daijie.jdbc.executor.SqlExecutor;
import org.daijie.jdbc.matedata.TableMatedata;
import org.daijie.jdbc.result.PageResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL分析器具体实现
 * @author daijie
 * @since 2019/5/23
 */
public class SqlAnalyzerImpl<T> implements SqlAnalyzer<T> {

    /**
     * SQL脚本操作类型
     */
    private SqlExecutor.Type type;

    /**
     * SQL语句
     */
    private String sql;

    /**
     * 查询总数SQL语句
     */
    private String countSql;

    /**
     * 占位符对应的参数
     */
    private List<Object> params = new ArrayList<>();

//    protected String generatingSql(TableMatedata table, T entity, Wrapper wrapper) {
//        StackTraceElement element = Thread.currentThread().getStackTrace()[1];
//        String parentMethodName = element.getMethodName();
//        Class cls = null;
//        try {
//            cls = Class.forName(element.getClassName());
//            for (Method method : cls.getMethods()) {
//                if (parentMethodName.substring(0, parentMethodName.lastIndexOf('(')).endsWith(method.getName())) {
//                    return generatingSql(table, entity, method, wrapper);
//                }
//                break;
//            }
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

    @Override
    public void generatingSql(TableMatedata table, T entity, Method method, Wrapper wrapper) {
        this.sql = sqlSpelling(table, entity, method, wrapper);
        if (sql.startsWith("select")) {
            this.type = SqlExecutor.Type.QUERY;
            if (method.getReturnType() == PageResult.class) {
                try {
                    this.countSql = sqlSpelling(table, entity, method.getDeclaringClass().getDeclaredMethod("selectCountByWrapper", Wrapper.class), wrapper.pageAndOrderClear());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.type = SqlExecutor.Type.UPDATE;
        }
    }

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

    /**
     * SQL分析拼接
     * @param table 表元数据
     * @param entity 映射对象
     * @param method mapper方法
     * @param wrapper 包装工具
     * @return String SQL语句
     */
    private String sqlSpelling(TableMatedata table, T entity, Method method, Wrapper wrapper) {
        StringBuilder sql = new StringBuilder();
        String methodName = method.getName();
        SqlSpelling sqlSpelling = SqlSpelling.getInstance();
        if (method.getAnnotation(Select.class) != null || (method.getAnnotation(Select.class) == null && methodName.startsWith("select"))) {
            if((method.getAnnotation(Select.class) != null && method.getAnnotation(Select.class).isCount()) || (method.getAnnotation(Select.class) == null && methodName.startsWith("selectCount"))) {
                sqlSpelling.selectCountSql(sql, table);
            } else {
                sqlSpelling.selectSql(sql, table);
            }
            if (entity != null) {
                sqlSpelling.whereSql(sql, table, entity);
                setParams(table, entity, sqlSpelling);
            } else if (wrapper != null) {
                sqlSpelling.whereSql(sql, table, wrapper, params).finalSql(sql, table, wrapper);
            }
        } else if(method.getAnnotation(Update.class) != null || (method.getAnnotation(Update.class) == null && methodName.startsWith("update"))) {
            T setEntity = null;
            T whereEntity = null;
            try {
                table.getPrimaryKey().getField().setAccessible(true);
                setEntity = (T) table.getEntityClass().newInstance();
                whereEntity = (T) table.getEntityClass().newInstance();
                table.getPrimaryKey().getField().set(whereEntity, table.getPrimaryKey().getField().get(entity));
                BeanUtil.copyProperties(entity, setEntity);
                table.getPrimaryKey().getField().set(setEntity, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sqlSpelling.updateSql(sql, table, setEntity);
            setParams(table, setEntity, sqlSpelling);
            if (wrapper == null) {
                sqlSpelling.whereSql(sql, table, whereEntity);
                setParams(table, whereEntity, sqlSpelling);
            } else {
                sqlSpelling.whereSql(sql, table, wrapper, params);
            }

        } else if(method.getAnnotation(Insert.class) != null || (method.getAnnotation(Insert.class) == null && methodName.startsWith("insert"))) {
            if((method.getAnnotation(Insert.class) != null && method.getAnnotation(Insert.class).isSelective()) || (method.getAnnotation(Insert.class) == null && methodName.startsWith("insertSelective"))) {
                sqlSpelling.insertSelectiveSql(sql, table, entity);
                setParams(table, entity, sqlSpelling);
            } else {
                sqlSpelling.insertSql(sql, table, entity);
                if (entity instanceof List) {
                    setAllParams(table, (List) entity, sqlSpelling);
                } else {
                    setAllParams(table, entity, sqlSpelling);
                }
            }
        } else if(method.getAnnotation(Delete.class) != null || (method.getAnnotation(Delete.class) == null && methodName.startsWith("delete"))) {
            sqlSpelling.deleteSql(sql, table);
            if (entity != null) {
                sqlSpelling.whereSql(sql, table, entity);
                setParams(table, entity, sqlSpelling);
            } else if (wrapper != null) {
                sqlSpelling.whereSql(sql, table, wrapper, params);
            }
        }
        return sql.toString();
    }

    /**
     * 设置占位符对应的参数，只添加映射对象中有值的字段
     * @param table 表元数据
     * @param entity 映射对象
     * @param sqlSpelling SQL拼接类
     */
    public void setParams(TableMatedata table, T entity, SqlSpelling sqlSpelling) {
        sqlSpelling.getFieldValue(table, entity).entrySet().forEach(entry -> {
            this.params.add(entry.getValue());
        });
    }

    /**
     * 设置占位符对应的参数，只添加映射对象中所有的字段
     * @param table 表元数据
     * @param entity 映射对象
     * @param sqlSpelling SQL拼接类
     */
    public void setAllParams(TableMatedata table, T entity, SqlSpelling sqlSpelling) {
        sqlSpelling.getAllFieldValue(table, entity).entrySet().forEach(entry -> {
            this.params.add(entry.getValue());
        });
    }

    /**
     * 设置占位符对应的参数，只添加映射对象中所有的字段
     * @param table 表元数据
     * @param entities 映射对象集
     * @param sqlSpelling SQL拼接类
     */
    public void setAllParams(TableMatedata table, List<T> entities, SqlSpelling sqlSpelling) {
        entities.forEach(entity -> {
            sqlSpelling.getAllFieldValue(table, entity).entrySet().forEach(entry -> {
                this.params.add(entry.getValue());
            });
        });
    }
}
