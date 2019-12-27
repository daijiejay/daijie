package org.daijie.jdbc.scripting;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * SQL条件包装工具
 * @author daijie
 * @since 2019/6/25
 */
public class Wrapper {

    /**
     * 包装条件构建
     */
    private WrapperBuilder wrapperBuilder;

    public Wrapper(WrapperBuilder wrapperBuilder) {
        this.wrapperBuilder = wrapperBuilder;
    }

    /**
     * 实例包装条件构建
     * @return 条件包装
     */
    public static Wrapper newWrapper() {
        return new Wrapper(new WrapperBuilder());
    }

    /**
     * 获取实例的包装条件构建
     * @return 包装条件构建
     */
    protected WrapperBuilder getWrapperBuilder() {
        return wrapperBuilder;
    }

    /**
     *  通用附加条件，用于判断达成某个条件后再会加SQL条件
     * @param obj 需要做判断的具体对象
     * @param predicate 对具体对象做校验
     * @param consumer 对条件包装类附加条件
     * @return 条件包装
     */
    public Wrapper and(Object obj, Predicate<Object> predicate, Consumer<Wrapper> consumer) {
        if(predicate.test(obj)) {
            consumer.accept(this);
        }
        return this;
    }

    /**
     *  通用附加条件，用于判断达成某个条件后再会加SQL条件
     * @param isWrapper 是否需要对条件包装类附加条件
     * @param consumer 对条件包装类附加条件
     * @return 条件包装
     */
    public Wrapper and(boolean isWrapper, Consumer<Wrapper> consumer) {
        if(isWrapper) {
            consumer.accept(this);
        }
        return this;
    }

    /**
     * 且条件，与对应的映射字段值相等
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " = ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，与对应的映射字段值相等
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " = ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，与对应的映射字段值不相等
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andNotEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <> ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，与对应的映射字段值不相等
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orNotEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <> ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值大于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andGreaterThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " > ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值大于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orGreaterThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " > ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值大于等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andGreaterThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " >= ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值大于等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orGreaterThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " >= ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值小于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andLessThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " < ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值小于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orLessThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " < ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值小于等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andLessThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <= ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值小于等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orLessThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <= ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值包括条件值数组中的所有值
     * @param property  映射字段
     * @param values 条件值数组
     * @return 条件包装
     */
    public Wrapper andIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " in ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值包括条件值数组中的所有值
     * @param property  映射字段
     * @param values 条件值数组
     * @return 条件包装
     */
    public Wrapper orIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " in ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值不包括条件值数组中的所有值
     * @param property  映射字段
     * @param values 条件值数组
     * @return 条件包装
     */
    public Wrapper andNotIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not in ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值不包括条件值数组中的所有值
     * @param property  映射字段
     * @param values 条件值数组
     * @return 条件包装
     */
    public Wrapper orNotIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not in ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值糊糊匹配不等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andNotLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " not like ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值糊糊匹配不等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orNotLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " not like ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值在条件值1与条件值2的区间中
     * @param property  映射字段
     * @param value1 条件值1
     * @param value2 条件值2
     * @return 条件包装
     */
    public Wrapper andBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " between ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值在条件值1与条件值2的区间中
     * @param property  映射字段
     * @param value1 条件值1
     * @param value2 条件值2
     * @return 条件包装
     */
    public Wrapper orBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " between ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值不在条件值1与条件值2的区间中
     * @param property  映射字段
     * @param value1 条件值1
     * @param value2 条件值2
     * @return 条件包装
     */
    public Wrapper andNotBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not between ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值不在条件值1与条件值2的区间中
     * @param property  映射字段
     * @param value1 条件值1
     * @param value2 条件值2
     * @return 条件包装
     */
    public Wrapper orNotBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not between ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，对应的映射字段值糊糊匹配等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper andLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " like ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，对应的映射字段值糊糊匹配等于条件值
     * @param property  映射字段
     * @param value 条件值
     * @return 条件包装
     */
    public Wrapper orLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " like ", WhereType.OR));
        return this;
    }

    /**
     * 且条件，再实例一个构建条件包装类在括号中添加条件
     * @param wrapper  条件包装
     * @return 条件包装
     */
    public Wrapper andBracket(Wrapper wrapper) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, WhereType.AND));
        return this;
    }

    /**
     * 或条件，再实例一个构建条件包装类在括号中添加条件
     * @param wrapper  条件包装
     * @return 条件包装
     */
    public Wrapper orBracket(Wrapper wrapper) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, WhereType.OR));
        return this;
    }

    /**
     * 且条件，存在指定映射对象的数据中
     * @param wrapper 条件包装
     * @param entityClass 子查询对应的映射对象类
     * @param equalNames 与子查询对应的映秀对象相等的字段名的二维数组，下标0为父对象字段名，下标2为子对象字段然
     * @return 条件包装
     */
    public Wrapper andExists(Wrapper wrapper, Class<?> entityClass, String[][] equalNames) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, entityClass, equalNames, "exists ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，存在指定映射对象的数据中
     * @param wrapper 条件包装
     * @param entityClass 子查询对应的映射对象类
     * @param equalNames 与子查询对应的映秀对象相等的字段名的二维数组，下标0为父对象字段名，下标2为子对象字段然
     * @return 条件包装
     */
    public Wrapper orExists(Wrapper wrapper, Class<?> entityClass, String[][] equalNames) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, entityClass, equalNames, "exists ",WhereType.OR));
        return this;
    }

    /**
     * 且条件，不存在指定映射对象的数据中
     * @param wrapper 条件包装
     * @param entityClass 子查询对应的映射对象类
     * @param equalNames 与子查询对应的映秀对象相等的字段名的二维数组，下标0为父对象字段名，下标2为子对象字段然
     * @return 条件包装
     */
    public Wrapper andNotExists(Wrapper wrapper, Class<?> entityClass, String[][] equalNames) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, entityClass, equalNames, "not exists ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，不存在指定映射对象的数据中
     * @param wrapper 条件包装
     * @param entityClass 子查询对应的映射对象类
     * @param equalNames 与子查询对应的映秀对象相等的字段名的二维数组，下标0为父对象字段名，下标2为子对象字段然
     * @return 条件包装
     */
    public Wrapper orNotExists(Wrapper wrapper, Class<?> entityClass, String[][] equalNames) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, entityClass, equalNames, "not exists ",WhereType.OR));
        return this;
    }

    /**
     * 且条件，正则表达式匹配
     * @param property 映射字段
     * @param regexp 正则表达式
     * @return 条件包装
     */
    public Wrapper andRegexp(String property, String regexp) {
        this.wrapperBuilder.conditions.add(new Condition(property, regexp, " regexp ", WhereType.AND));
        return this;
    }

    /**
     * 或条件，正则表达式匹配
     * @param property 映射字段
     * @param regexp 正则表达式
     * @return 条件包装
     */
    public Wrapper orRegexp(String property, String regexp) {
        this.wrapperBuilder.conditions.add(new Condition(property, regexp, " regexp ", WhereType.OR));
        return this;
    }

    /**
     * 指定映秀对象字段升序排序
     * @param property 映秀对象字段名
     * @return 条件包装
     */
    public Wrapper orderByAsc(String property) {
        this.wrapperBuilder.addOrders(property, OrderType.ASC);
        return this;
    }

    /**
     * 指定映秀对象字段降序排序
     * @param property 映秀对象字段名
     * @return 条件包装
     */
    public Wrapper orderByDesc(String property) {
        this.wrapperBuilder.addOrders(property, OrderType.DESC);
        return this;
    }

    /**
     * 指定映秀对象多个字段分组
     * @param properties 映秀对象字段名数组
     * @return 条件包装
     */
    public Wrapper groupBy(String... properties) {
        this.wrapperBuilder.addGroups(properties);
        return this;
    }

    /**
     * 查询分页
     * @param pageNumber 当前页
     * @param pageSize 条数
     * @return 条件包装
     */
    public Wrapper page(int pageNumber, int pageSize) {
        this.wrapperBuilder.setPage(new Page(pageNumber, pageSize));
        return this;
    }

    /**
     * 清除分页和排序的包装
     * @return 条件包装
     */
    protected Wrapper pageAndOrderClear() {
        this.wrapperBuilder.setPage(null);
        this.wrapperBuilder.clearOrders();
        return this;
    }

    /**
     * 条件包装构建类，封装查询条件、排序、分组、分页的相关信息
     */
    public static class WrapperBuilder {

        /**
         * 条件集
         */
        private List<Condition> conditions = new ArrayList<>();

        /**
         * 排序集，key为排序字段名，value为排序类型
         */
        private Map<String, OrderType> orders = new HashMap<>();

        /**
         * 分组字段集
         */
        private List<String> groups = new ArrayList<>();

        /**
         * 分页
         */
        private Page page;

        protected void addCodition(Condition condition) {
            this.conditions.add(condition);
        }

        protected List<Condition> getConditions() {
            return conditions;
        }

        protected void addOrders(String columnName, OrderType orderType) {
            this.orders.put(columnName, orderType);
        }

        protected Map<String, OrderType> getOrders() {
            return orders;
        }

        protected void clearOrders() {
            this.orders.clear();
        }

        protected void addGroups(String... columnNames) {
            this.groups.clear();
            this.groups.addAll(Arrays.asList(columnNames));
        }

        protected List<String> getGroups() {
            return groups;
        }

        protected Page getPage() {
            return page;
        }

        protected void setPage(Page page) {
            this.page = page;
        }
    }

    /**
     * 条件信息包装
     */
    protected static class Condition {

        /**
         * 映射对象字段集
         */
        private String[] columnNames;

        /**
         * 条件值集
         */
        private Object[] columnValues;

        /**
         * 子查询条件包装
         */
        private Wrapper wrapper;

        /**
         * 子查询条件指定子映射对象类
         */
        private Class<?> entityClass;

        /**
         * 与子查询对应的映秀对象相等的字段名的二维数组，下标0为父对象字段名，下标2为子对象字段然
         */
        private String[][] equalNames;

        /**
         * sql脚本符号
         */
        private String fix;

        /**
         * 条件分类
         */
        private ConditionType conditionType;

        /**
         * where分类
         */
        private WhereType whereType;

        protected Condition(String columnName, Object columnValue, String fix, WhereType whereType) {
            this.columnNames = new String[1];
            this.columnNames[0] = columnName;
            this.columnValues = new Object[1];
            this.columnValues[0] = columnValue;
            this.fix = fix;
            this.whereType = whereType;
            this.conditionType = ConditionType.COMMON;
        }

        protected Condition(String columnName, Object[] columnValues, String fix, WhereType whereType) {
            this.columnNames = new String[1];
            this.columnNames[0] = columnName;
            this.columnValues = columnValues;
            this.fix = fix;
            this.whereType = whereType;
            this.conditionType = ConditionType.BETWEEN;
        }

        protected Condition(Wrapper wrapper, WhereType whereType) {
            this.wrapper = wrapper;
            this.whereType = whereType;
            this.conditionType = ConditionType.BRACKET;
        }

        protected Condition(Wrapper wrapper, Class<?> entityClass, String[][] equalNames, String fix, WhereType whereType) {
            this.equalNames = equalNames;
            this.fix = fix;
            this.wrapper = wrapper;
            this.entityClass = entityClass;
            this.whereType = whereType;
            this.conditionType = ConditionType.EXISTS;
        }

        protected String[] getColumnNames() {
            return columnNames;
        }

        protected Object[] getColumnValues() {
            return columnValues;
        }

        protected Wrapper getWrapper() {
            return wrapper;
        }

        protected Class<?> getEntityClass() {
            return entityClass;
        }

        protected String[][] getEqualNames() {
            return equalNames;
        }

        protected String getFix() {
            return fix;
        }

        protected ConditionType getConditionType() {
            return conditionType;
        }

        protected WhereType getWhereType() {
            return whereType;
        }
    }

    /**
     * 分页对象
     */
    protected static class Page {

        /**
         * 当前页
         */
        private int pageNumber;

        /**
         * 页条数
         */
        private int pageSize;

        protected Page(int pageNumber, int pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        protected int getPageNumber() {
            return pageNumber;
        }

        protected int getPageSize() {
            return pageSize;
        }
    }

    /**
     * 条件分类
     */
    protected enum ConditionType {
        /**
         * 普通条件，包换and/or
         */
        COMMON,

        /**
         * 区间条件，包装between
         */
        BETWEEN,

        /**
         * 括号引用条件
         */
        BRACKET,

        /**
         * 存在子表条件
         */
        EXISTS
    }

    /**
     * where分类
     */
    protected enum WhereType {
        /**
         * 且条件
         */
        AND,

        /**
         * 或条件
         */
        OR
    }

    /**
     * 排序类型
     */
    protected enum OrderType {
        /**
         * 升序
         */
        ASC,

        /**
         * 降序
         */
        DESC
    }
}
