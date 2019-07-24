package org.daijie.jdbc.matedata;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 表元数据
 * @author daijie
 * @since 2019/5/23
 */
public class TableMatedata {

    /**
     * 表名
     */
    private final String name;

    /**
     * 表映射对象类型
     */
    private final Class entityClass;

    /**
     * 表列元数据数组
     * key值为映射对象字段名
     */
    private final Map<String, ColumnMateData> columns = new HashMap();

    /**
     * 表主键列元数据
     */
    private ColumnMateData primaryKey;

    /**
     * 表映射返回对象
     */
    private Class returnClass;

    /**
     * 表列元数据数组（自定义返回对象对应字段）
     * key值为映射对象字段名
     */
    private final Map<String, ColumnMateData> resultColumns = new HashMap();

    /**
     * 构建表元数据
     * @param name 表名
     * @param entityClass 表映射对象类型
     */
    public TableMatedata(String name, Class entityClass) {
        this.name = name;
        this.entityClass = entityClass;
    }

    public String getName() {
        return this.name;
    }

    public void addColumn(ColumnMateData col) {
        addColumn(col.getName().toUpperCase(Locale.ENGLISH), col);
    }

    public void addColumn(String name, ColumnMateData col) {
        this.columns.put(name.toUpperCase(Locale.ENGLISH), col);
    }

    public ColumnMateData getColumn(String name) {
        return (ColumnMateData)this.columns.get(name.toUpperCase(Locale.ENGLISH));
    }

    public Map<String, ColumnMateData> getColumns() {
        return this.columns;
    }

    public String[] getColumnNames() {
        return (String[])this.columns.keySet().toArray(new String[this.columns.size()]);
    }

    public void setPrimaryKey(ColumnMateData column) {
        this.primaryKey = column;
    }

    public ColumnMateData getPrimaryKey() {
        return this.primaryKey;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setReturnClass(Class returnClass) {
        this.returnClass = returnClass;
    }

    public Class getReturnClass() {
        return this.returnClass;
    }

    public boolean isCostomResult() {
        return this.returnClass != null;
    }

    public void addResultColumn(String name, ColumnMateData col) {
        this.resultColumns.put(name.toUpperCase(Locale.ENGLISH), col);
    }

    public Map<String, ColumnMateData> getResultColumns() {
        return this.resultColumns;
    }

    public Map<String, ColumnMateData> getDefaultColumns() {
        if (isCostomResult()) {
            return this.resultColumns;
        }
        return this.columns;
    }
}
