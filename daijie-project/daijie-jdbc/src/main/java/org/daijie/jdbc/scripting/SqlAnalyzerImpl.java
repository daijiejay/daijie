package org.daijie.jdbc.scripting;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.daijie.jdbc.annotation.*;
import org.daijie.jdbc.executor.SqlExecutor;
import org.daijie.jdbc.matedata.MultiTableMateData;
import org.daijie.jdbc.matedata.TableMateData;
import org.daijie.jdbc.ognl.ParseScriptExpression;
import org.daijie.jdbc.result.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SQL分析器具体实现
 * @author daijie
 * @since 2019/5/23
 */
public class SqlAnalyzerImpl<T> implements SqlAnalyzer<T> {

    private final Logger log = LoggerFactory.getLogger(SqlAnalyzerImpl.class);

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

    @Override
    public void generatingSql(TableMateData table, T entity, Method method, Wrapper wrapper) {
        this.sql = createSqlAnnotation(method, entity);
        if (StringUtils.isEmpty(this.sql)) {
            this.sql = sqlSpelling(table, entity, method, wrapper);
        }
        if (this.sql.startsWith("select")) {
            this.type = SqlExecutor.Type.QUERY;
        } else {
            this.type = SqlExecutor.Type.UPDATE;
        }
    }

    @Override
    public void generatingSql(MultiTableMateData table, Method method, MultiWrapper multiWrapper) {
        SqlSpelling sqlSpelling = SqlSpelling.getInstance();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        if (PageResult.class.isAssignableFrom(method.getReturnType())) {
            StringBuilder countSql = new StringBuilder();
            sqlSpelling.agileSql(countSql, table, multiWrapper, Lists.newArrayList(), true);
            this.countSql = countSql.toString();
        }
        sqlSpelling.agileSql(sql, table, multiWrapper, params, false);
        this.sql = sql.toString();
        this.type = SqlExecutor.Type.QUERY;
    }

    /**
     * 通过注解中配置的SQL分析可以执行的SQL语句
     * @param method 方法
     * @param entity 方法参数
     * @return SQL语句
     */
    private String createSqlAnnotation(Method method, T entity) {
        final Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<Class<? extends Annotation>>();
        sqlAnnotationTypes.add(Select.class);
        sqlAnnotationTypes.add(Update.class);
        sqlAnnotationTypes.add(Insert.class);
        sqlAnnotationTypes.add(Delete.class);
        Class<? extends Annotation> sqlAnnotationType = chooseAnnotationType(method, sqlAnnotationTypes);
        if (sqlAnnotationType != null) {
            try {
                Annotation sqlAnnotation = method.getAnnotation(sqlAnnotationType);
                String script = (String) sqlAnnotation.getClass().getMethod("value").invoke(sqlAnnotation);
                String[] parameters = getMethodParameters(method);
                ParseScriptExpression parseScriptExpression = new ParseScriptExpression(script, entity, parameters);
                return parseScriptExpression.parse(this.params);
            } catch (Exception e){
                log.error("脚本语法错误", e);
            }
        }
        return null;
    }

    /**
     * 获取方法在指定注解集中第一个存在的注解
     * @param method 方法
     * @param types 指定注解集
     * @return 方法修饰的注解
     */
    private Class<? extends Annotation> chooseAnnotationType(Method method, Set<Class<? extends Annotation>> types) {
        for (Class<? extends Annotation> type : types) {
            Annotation annotation = method.getAnnotation(type);
            if (annotation != null) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取方法参数名
     * @param method 方法
     * @return 方法参数名
     */
    private String[] getMethodParameters(Method method) {
        String[] parameters = new String[method.getParameters().length];
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i ++) {
            Annotation[] annotations = parameterAnnotations[i];
            boolean annotationFlag = false;
            for (int j = 0; j < annotations.length; j ++) {
                if (annotations[j] instanceof Param) {
                    parameters[i] = ((Param) annotations[j]).value();
                    annotationFlag = true;
                }
            }
            if (!annotationFlag) {
                parameters[i] = method.getParameters()[i].getName();
            }
        }
        return parameters;
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
    private String sqlSpelling(TableMateData table, T entity, Method method, Wrapper wrapper) {
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSql = new StringBuilder();
        String methodName = method.getName();
        SqlSpelling sqlSpelling = SqlSpelling.getInstance();
        if (method.getAnnotation(Select.class) != null || (method.getAnnotation(Select.class) == null && methodName.startsWith("select"))) {
            if((method.getAnnotation(Select.class) != null && method.getAnnotation(Select.class).isCount()) || (method.getAnnotation(Select.class) == null && methodName.startsWith("selectCount"))) {
                sqlSpelling.selectCountSql(sql, table);
            } else {
                sqlSpelling.selectSql(sql, table);
            }
            if (entity != null) {
                sqlSpelling.whereSql(whereSql, table, entity);
                sql.append(whereSql);
                setParams(table, entity, sqlSpelling);
            } else if (wrapper != null) {
                sqlSpelling.whereSql(whereSql, table, wrapper, params);
                sql.append(whereSql);
                sqlSpelling.finalSql(sql, table, wrapper);
            }
            if (PageResult.class.isAssignableFrom(method.getReturnType())) {
                StringBuilder countSql = new StringBuilder();
                sqlSpelling.selectCountSql(countSql, table);
                countSql.append(whereSql);
                if (wrapper != null) {
                    sqlSpelling.finalSql(sql, table, wrapper.pageAndOrderClear());
                }
                this.countSql = countSql.toString();
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
            if((method.getAnnotation(Update.class) != null && method.getAnnotation(Update.class).isSelective()) || (method.getAnnotation(Update.class) == null && methodName.startsWith("updateSelective"))) {
                sqlSpelling.updateSelectiveSql(sql, table, setEntity);
                setParams(table, setEntity, sqlSpelling);
            } else {
                sqlSpelling.updateSql(sql, table, setEntity);
                setAllParams(table, entity, sqlSpelling);
            }
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
    public void setParams(TableMateData table, T entity, SqlSpelling sqlSpelling) {
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
    public void setAllParams(TableMateData table, T entity, SqlSpelling sqlSpelling) {
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
    public void setAllParams(TableMateData table, List<T> entities, SqlSpelling sqlSpelling) {
        entities.forEach(entity -> {
            sqlSpelling.getAllFieldValue(table, entity).entrySet().forEach(entry -> {
                this.params.add(entry.getValue());
            });
        });
    }
}
