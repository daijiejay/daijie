package org.daijie.jdbc.scripting;

import java.util.*;

/**
 * SQL条件包装工具
 * @author daijie
 * @since 2019/6/25
 */
public class Wrapper {

    private WrapperBuilder wrapperBuilder;

    public Wrapper(WrapperBuilder wrapperBuilder) {
        this.wrapperBuilder = wrapperBuilder;
    }

    public static Wrapper newWrapper() {
        return new Wrapper(new WrapperBuilder());
    }

    protected WrapperBuilder getWrapperBuilder() {
        return wrapperBuilder;
    }

    public Wrapper andEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " = ", WhereType.AND));
        return this;
    }

    public Wrapper orEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " = ", WhereType.OR));
        return this;
    }

    public Wrapper andNotEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <> ", WhereType.AND));
        return this;
    }

    public Wrapper orNotEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <> ", WhereType.OR));
        return this;
    }

    public Wrapper andGreaterThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " > ", WhereType.AND));
        return this;
    }

    public Wrapper orGreaterThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " > ", WhereType.OR));
        return this;
    }

    public Wrapper andGreaterThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " >= ", WhereType.AND));
        return this;
    }

    public Wrapper orGreaterThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " >= ", WhereType.OR));
        return this;
    }

    public Wrapper andLessThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " < ", WhereType.AND));
        return this;
    }

    public Wrapper orLessThan(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " < ", WhereType.OR));
        return this;
    }

    public Wrapper andLessThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <= ", WhereType.AND));
        return this;
    }

    public Wrapper orLessThanOrEqualTo(String property, Object value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " <= ", WhereType.OR));
        return this;
    }

    public Wrapper andIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " in ", WhereType.AND));
        return this;
    }

    public Wrapper orIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " in ", WhereType.OR));
        return this;
    }

    public Wrapper andNotIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not in ", WhereType.AND));
        return this;
    }

    public Wrapper orNotIn(String property, Iterable values) {
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not in ", WhereType.OR));
        return this;
    }

    public Wrapper andNotLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " not like ", WhereType.AND));
        return this;
    }

    public Wrapper orNotLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " not like ", WhereType.OR));
        return this;
    }

    public Wrapper andBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " between ", WhereType.AND));
        return this;
    }

    public Wrapper orBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " between ", WhereType.OR));
        return this;
    }

    public Wrapper andNotBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not between ", WhereType.AND));
        return this;
    }

    public Wrapper orNotBetween(String property, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        this.wrapperBuilder.conditions.add(new Condition(property, values, " not between ", WhereType.OR));
        return this;
    }

    public Wrapper andLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " like ", WhereType.AND));
        return this;
    }

    public Wrapper orLike(String property, String value) {
        this.wrapperBuilder.conditions.add(new Condition(property, value, " like ", WhereType.OR));
        return this;
    }

    public Wrapper andBracket(Wrapper wrapper) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, WhereType.AND));
        return this;
    }

    public Wrapper orBracket(Wrapper wrapper) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, WhereType.OR));
        return this;
    }

    public Wrapper andExists(Wrapper wrapper, Class<?> entityClass, String[][] equalNames) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, entityClass, equalNames, WhereType.AND));
        return this;
    }

    public Wrapper orExists(Wrapper wrapper, Class<?> entityClass, String[][] equalNames) {
        this.wrapperBuilder.conditions.add(new Condition(wrapper, entityClass, equalNames, WhereType.OR));
        return this;
    }

    public Wrapper orderByAsc(String property) {
        this.wrapperBuilder.addOrders(property, OrderType.ASC);
        return this;
    }

    public Wrapper orderByDesc(String property) {
        this.wrapperBuilder.addOrders(property, OrderType.DESC);
        return this;
    }

    public Wrapper groupBy(String... properties) {
        this.wrapperBuilder.addGroups(properties);
        return this;
    }

    public Wrapper page(int pageNumber, int pageSize) {
        this.wrapperBuilder.setPage(new Page(pageNumber, pageSize));
        return this;
    }

    protected Wrapper pageAndOrderClear() {
        this.wrapperBuilder.setPage(null);
        this.wrapperBuilder.clearOrders();
        return this;
    }

    public static class WrapperBuilder {

        private List<Condition> conditions = new ArrayList<>();

        private Map<String, OrderType> orders = new HashMap<>();

        private List<String> groups = new ArrayList<>();

        private Page page;

        protected void addCodition(Condition condition) {
            this.conditions.add(condition);
        }

        public List<Condition> getConditions() {
            return conditions;
        }

        protected void addOrders(String columnName, OrderType orderType) {
            this.orders.put(columnName, orderType);
        }

        public Map<String, OrderType> getOrders() {
            return orders;
        }

        protected void clearOrders() {
            this.orders.clear();
        }

        protected void addGroups(String... columnNames) {
            this.groups.clear();
            this.groups.addAll(Arrays.asList(columnNames));
        }

        public List<String> getGroups() {
            return groups;
        }

        public Page getPage() {
            return page;
        }

        protected void setPage(Page page) {
            this.page = page;
        }
    }

    protected static class Condition {

        private String[] columnNames;

        private Object[] columnValues;

        private Wrapper wrapper;

        private Class<?> entityClass;

        private String[][] equalNames;

        private String fix;

        private ConditionType conditionType;

        private WhereType whereType;

        public Condition(String columnName, Object columnValue, String fix, WhereType whereType) {
            this.columnNames = new String[1];
            this.columnNames[0] = columnName;
            this.columnValues = new Object[1];
            this.columnValues[0] = columnValue;
            this.fix = fix;
            this.whereType = whereType;
            this.conditionType = ConditionType.COMMON;
        }

        public Condition(String columnName, Object[] columnValues, String fix, WhereType whereType) {
            this.columnNames = new String[1];
            this.columnNames[0] = columnName;
            this.columnValues = columnValues;
            this.fix = fix;
            this.whereType = whereType;
            this.conditionType = ConditionType.BETWEEN;
        }

        public Condition(Wrapper wrapper, WhereType whereType) {
            this.wrapper = wrapper;
            this.whereType = whereType;
            this.conditionType = ConditionType.BRACKET;
        }

        public Condition(Wrapper wrapper, Class<?> entityClass, String[][] equalNames, WhereType whereType) {
            this.equalNames = equalNames;
            this.wrapper = wrapper;
            this.entityClass = entityClass;
            this.whereType = whereType;
            this.conditionType = ConditionType.EXISTS;
        }

        public String[] getColumnNames() {
            return columnNames;
        }

        public Object[] getColumnValues() {
            return columnValues;
        }

        public Wrapper getWrapper() {
            return wrapper;
        }

        public Class<?> getEntityClass() {
            return entityClass;
        }

        public String[][] getEqualNames() {
            return equalNames;
        }

        public String getFix() {
            return fix;
        }

        public ConditionType getConditionType() {
            return conditionType;
        }

        public WhereType getWhereType() {
            return whereType;
        }
    }

    protected static class Page {

        private int pageNumber;

        private int pageSize;

        public Page(int pageNumber, int pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }
    }

    protected enum ConditionType {
        COMMON,
        BETWEEN,
        BRACKET,
        EXISTS
    }

    protected enum WhereType {
        AND,
        OR
    }

    protected enum OrderType {
        ASC,
        DESC
    }
}
