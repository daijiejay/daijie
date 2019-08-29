package org.daijie.jdbc.scripting;

import org.daijie.jdbc.matedata.AgileTableMateData;
import org.daijie.jdbc.matedata.TableMatedata;
import org.daijie.jdbc.matedata.TableMatedataManage;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL拼接
 * @author daijie
 * @since 2019/5/24
 */
public class SqlSpelling {

    /**
     * 构建一个SQL拼接类
     * @return SqlSpelling SQL拼接
     */
    public static SqlSpelling getInstance() {
        return new SqlSpelling();
    }

    /**
     * 查询SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling selectSql(StringBuilder sql, TableMatedata table) {
        sql.append("select ");
        columnsSql(sql, table);
        sql.append(" from ").append(table.getName());
        return this;
    }

    /**
     * 查询总数SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling selectCountSql(StringBuilder sql, TableMatedata table) {
        sql.append("select count(1)");
        sql.append(" from ").append(table.getName());
        return this;
    }

    /**
     * 查询结果字段SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling columnsSql(StringBuilder sql, TableMatedata table) {
        sql.append(collectionToCommaDelimitedString(table.getDefaultColumns().entrySet().stream().map(entry -> entry.getValue().getName()).collect(Collectors.toList())));
        return this;
    }

    /**
     * 条件SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @param entity 映射对象或包装对象
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling whereSql(StringBuilder sql, TableMatedata table, Object entity) {
        Map<String, Object> names = getFieldValue(table, entity);
        if (names.size() > 0) {
            sql.append(" where ").append(collectionToDelimitedString(names.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()), " = ? and "));
            sql.append(" = ?");
        }
        return this;
    }

    /**
     * 条件SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @param wrapper 包装条件对象
     * @param params 占位符对应的参数
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling whereSql(StringBuilder sql, TableMatedata table, Wrapper wrapper, List<Object> params) {
        if (wrapper.getWrapperBuilder().getConditions().size() > 0) {
            sql.append(" where ");
            wrapperConditions(sql, table, wrapper, params);
        }
        return this;
    }

    /**
     * 更新SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @param entity 映射对象
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling updateSql(StringBuilder sql, TableMatedata table, Object entity) {
        Map<String, Object> names = getFieldValue(table, entity);
        if (names.size() > 0) {
            sql.append("update ").append(table.getName()).append(" set ").append(collectionToDelimitedString(names.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()), " = ?, "));
            sql.append(" = ?");
        }
        return this;
    }

    /**
     * 插入SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @param data 映射对象单个或多个
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling insertSql(StringBuilder sql, TableMatedata table, Object data) {
        Object entity = null;
        int rows = 1;
        if (data instanceof List) {
            rows = ((List) data).size();
            entity = ((List) data).get(0);
        } else {
            entity = data;
        }
        Map<String, Object> names = getAllFieldValue(table, entity);
        if (names.size() > 0) {
            sql.append("insert into ").append(table.getName()).append(" (");
            sql.append(collectionToCommaDelimitedString(names.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList())));
            sql.append(") values ");
            StringBuilder inSql = new StringBuilder();
            inSql.append("(");
            String[] symbols = new String[names.size()];
            for (int i = 0; i < names.size(); i ++) {
                symbols[i] = "?";
            }
            inSql.append(collectionToDelimitedString(Arrays.asList(symbols), ","));
            inSql.append(")");
            sql.append(numberToDelimitedString(inSql.toString(), rows));
        }
        return this;
    }

    /**
     * 插入SQL拼接（只插入值不为null的字段）
     * @param sql SQL语句
     * @param table 表元数据
     * @param entity 映射对象
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling insertSelectiveSql(StringBuilder sql, TableMatedata table, Object entity) {
        Map<String, Object> names = getFieldValue(table, entity);
        if (names.size() > 0) {
            sql.append("insert into ").append(table.getName()).append(" (");
            sql.append(collectionToCommaDelimitedString(names.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList())));
            sql.append(") values (");
            String[] symbols = new String[names.size()];
            for (int i = 0; i < names.size(); i ++) {
                symbols[i] = "?";
            }
            sql.append(collectionToDelimitedString(Arrays.asList(symbols), ","));
            sql.append(")");
        }
        return this;
    }

    /**
     * 删除SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling deleteSql(StringBuilder sql, TableMatedata table) {
        sql.append("delete from ").append(table.getName());
        return this;
    }

    /**
     * SQL拼接，分组、排序、分页
     * @param sql SQL语句
     * @param table 表元数据
     * @param wrapper 包装条件对象
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling finalSql(StringBuilder sql, TableMatedata table, Wrapper wrapper) {
        wrapperGroups(sql, table, wrapper.getWrapperBuilder().getGroups());
        wrapperOrders(sql, table, wrapper.getWrapperBuilder().getOrders());
        wrapperPages(sql, table, wrapper.getWrapperBuilder().getPage());
        return this;
    }

    /**
     * 拼接分组SQL语句
     * @param sql SQL语句
     * @param table 表元数据
     * @param groups 分组字段集
     */
    private void wrapperGroups(StringBuilder sql, TableMatedata table, List<String> groups) {
        if (!groups.isEmpty()) {
            sql.append(" group by ");
            Iterator<String> it = groups.iterator();
            while (it.hasNext()) {
                String key = it.next();
                sql.append(table.getColumn(key).getName());
                if (it.hasNext()) {
                    sql.append(", ");
                }
            }
        }
    }

    /**
     * 拼接排序QL语句
     * @param sql SQL语句
     * @param table 表元数据
     * @param orders 排序字段集
     */
    private void wrapperOrders(StringBuilder sql, TableMatedata table, Map<String, Wrapper.OrderType> orders) {
        if (!orders.isEmpty()) {
            sql.append(" order by ");
            Iterator<String> it = orders.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                sql.append(table.getColumn(key).getName()).append(" ").append(orders.get(key));
                if (it.hasNext()) {
                    sql.append(", ");
                }
            }
        }
    }

    /**
     * 拼接分页SQL语句
     * @param sql SQL语句
     * @param table 表元数据
     * @param page 分页属性对象
     */
    private void wrapperPages(StringBuilder sql, TableMatedata table, Wrapper.Page page) {
        if (page != null) {
            sql.append(" limit ").append((page.getPageNumber() - 1) * page.getPageSize()).append(", ").append(page.getPageSize());
        }
    }

    /**
     * 根据wrapper拼接条件SAL语句
     * @param sql SQL语句
     * @param table 表元数据
     * @param wrapper 包装工具
     * @param params 占位符对应的参数
     * @return 返回拼接好的sql语句
     */
    public String  wrapperConditions(StringBuilder sql, TableMatedata table, Wrapper wrapper, List<Object> params) {
        Map<String, Wrapper.WhereType> spells = new HashMap();
        for (Wrapper.Condition condition : wrapper.getWrapperBuilder().getConditions()) {
            switch (condition.getConditionType()) {
                case COMMON:
                    Object value = condition.getColumnValues()[0];
                    if (value instanceof List) {
                        StringBuilder inSql = new StringBuilder();
                        inSql.append(table.getColumn(condition.getColumnNames()[0]).getName() + condition.getFix() + "(");
                        Iterator it = ((List) value).iterator();
                        while (it.hasNext()) {
                            params.add(it.next());
                            inSql.append("?");
                            if (it.hasNext()) {
                                inSql.append(",");
                            }
                        }
                        inSql.append(")");
                        spells.put(inSql.toString(), condition.getWhereType());
                    } else {
                        spells.put(table.getColumn(condition.getColumnNames()[0]).getName() + condition.getFix() + "?", condition.getWhereType());
                        params.add(condition.getColumnValues()[0]);
                    }
                    break;
                case BETWEEN:
                    spells.put(table.getColumn(condition.getColumnNames()[0]).getName() + condition.getFix() + "? and" + "?", condition.getWhereType());
                    params.add(condition.getColumnValues()[0]);
                    params.add(condition.getColumnValues()[1]);
                    break;
                case BRACKET:
                    StringBuilder bracketSql = new StringBuilder();
                    bracketSql.append("(");
                    bracketSql.append(wrapperConditions(new StringBuilder(), table, condition.getWrapper(), params));
                    bracketSql.append(")");
                    spells.put(bracketSql.toString(), condition.getWhereType());
                    break;
                case EXISTS:
                    StringBuilder existsSql = new StringBuilder();
                    existsSql.append(" exists (");
                    childSelectSql(existsSql, table, TableMatedataManage.initTable(condition.getEntityClass()), condition.getWrapper(), condition.getEqualNames(), params);
                    existsSql.append(")");
                    spells.put(existsSql.toString(), condition.getWhereType());
            }
        }
        sql.append(mapToDelimitedString(spells));
        return sql.toString();
    }

    /**
     * 拼接子查询SQL语句
     * @param sql SQL语句
     * @param parentTable 父级表元数据
     * @param childTable 子级表元数据
     * @param wrapper 条件包装工具
     * @param equalNames 父级与子级关联条件值，key为父级字段，value为子级字段
     * @param params 占位符对应的参数
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling childSelectSql(StringBuilder sql, TableMatedata parentTable, TableMatedata childTable, Wrapper wrapper, String[][] equalNames, List<Object> params) {
        sql.append("select 1");
        sql.append(" from ").append(childTable.getName());
        sql.append(" where ");
        List<String> equalSql = new ArrayList<>();
        for (String[] equalName : equalNames) {
            equalSql.add(parentTable.getName() + "." + parentTable.getColumn(equalName[0]).getName() + " = " + childTable.getName() + "." + childTable.getColumn(equalName[1]).getName());
        }
        sql.append(collectionToDelimitedString(equalSql, " and "));
        wrapperConditions(sql, childTable, wrapper, params);
        return this;
    }

    /**
     * 条件SQL拼接
     * @param sql SQL语句
     * @param table 表元数据
     * @param agileWrapper 包装条件对象，多表关联查询
     * @param params 占位符对应的参数
     * @return SqlSpelling SQL拼接
     */
    public SqlSpelling agileSql(StringBuilder sql, AgileTableMateData table, AgileWrapper agileWrapper, List<Object> params) {
        AgileWrapper.AgileWrapperBuilder wrapperBuilder = agileWrapper.getWrapperBuilder();
        Map<Class, AgileWrapper.JoinCondition> joining = wrapperBuilder.getJoining();
        Map<Class, Wrapper> wrapping =  wrapperBuilder.getWrapping();
        sql.append("select ");
        this.columnsSql(sql, table);
        sql.append(" from ");
        sql.append(table.getMateData(wrapperBuilder.getEntityClass()).getName());
        Iterator<Class> it = wrapperBuilder.getJoining().keySet().iterator();
        while (it.hasNext()) {

        }
        return this;
    }

    /**
     * 从映射对象中获取有值的字段
     * key是列字段名，value是字段值
     * @param table 表元数据
     * @param entity 映射对象
     * @return Map 得到已过滤值为空的字段对象
     */
    public Map<String, Object> getFieldValue(TableMatedata table, Object entity) {
        Map<String, Object> names = new LinkedHashMap<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object obj = field.get(entity);
                if (obj != null) {
                    names.put(table.getColumn(field.getName()).getName(), obj);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return names;
    }

    /**
     * 从映射对象中获取所有的字段，包括null值
     * key是列字段名，value是字段值
     * @param table 表元数据
     * @param entity 映射对象
     * @return Map 得到已过滤值为空的字段对象
     */
    public Map<String, Object> getAllFieldValue(TableMatedata table, Object entity) {
        Map<String, Object> names = new LinkedHashMap<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object obj = field.get(entity);
                names.put(table.getColumn(field.getName()).getName(), obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return names;
    }

    /**
     * 通过字符串拼接“,”获取字符串次数
     * @param str 要拼接的字符串
     * @return String 拼接完成后的字符串
     */
    public String numberToDelimitedString(@Nullable String str, int number) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < number; i ++) {
            sb.append(str);
            if (i < number - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 通过数组拼接“,”获取字符串
     * @param collection 要拼接数组
     * @return String 拼接完成后的字符串
     */
    public String collectionToCommaDelimitedString(@Nullable Collection<?> collection) {
        return collectionToDelimitedString(collection, ",");
    }

    /**
     * 通过数组拼接指定符号获取字符串
     * @param collection 要拼接数组
     * @param delim 指定符号
     * @return String 拼接完成后的字符串
     */
    public String collectionToDelimitedString(@Nullable Collection<?> collection, String delim) {
        return collectionToDelimitedString(collection, delim, "", "");
    }

    /**
     * 通过数组拼接指定符号获取字符串
     * @param collection 要拼接数组
     * @param delim 条件类型
     * @param prefix 字符串前缀
     * @param suffix 字符串后缀
     * @return String 拼接完成后的字符串
     */
    public String collectionToDelimitedString(@Nullable Collection<?> collection, String delim, String prefix, String suffix) {
        if (collection == null || collection.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = collection.iterator();
            while(it.hasNext()) {
                sb.append(prefix).append(it.next()).append(suffix);
                if (it.hasNext()) {
                    sb.append(delim);
                }
            }
            return sb.toString();
        }
    }

    /**
     * 拼接where子句
     * @param map key为条件语句，value为条件类型
     * @return String 拼接完成后的字符串
     */
    private String mapToDelimitedString(Map<String, Wrapper.WhereType> map) {
        if (map == null || map.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator<String> it =  map.keySet().iterator();
            while(it.hasNext()) {
                String key = it.next();
                sb.append(key);
                if (it.hasNext()) {
                    switch (map.get(key)) {
                        case AND:
                            sb.append(" and ");
                            break;
                        case OR:
                            sb.append(" or ");
                    }
                }
            }
            return sb.toString();
        }
    }
}
