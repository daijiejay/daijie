package org.daijie.jdbc.matedata;

import java.lang.reflect.Field;

/**
 * 表列元数据
 * @author daijie
 * @since 2019/5/23
 */
public class ColumnMateData {

    /**
     * 表名
     */
    private String table;

    /**
     * 表字段名
     */
    private String name;

    /**
     * 表字段注释
     */
    private String remarks;

    /**
     * 表字段类类型
     */
    private Class<?> javaType;

    /**
     * 表字段
     */
    private Field field;

    /**
     * 子表元数据
     */
    private TableMateData tableMatedata;

    /**
     * 表字段类型
     */
    private String columnType;

    /**
     * 表字段大小
     */
    private int columnSize;

    /**
     * 表字段长度
     */
    private int columnDecimalDigits;

    /**
     * 表字段是否可为空
     */
    private boolean nullable;

    /**
     * 构建表列元数据
     * @param name 表字段名
     */
    public ColumnMateData(String name) {
        this.name = name;
    }

    /**
     * 构建表列元数据
     * @param name 表字段名
     * @param javaType 表字段类类型
     * @param field 表字段
     */
    public ColumnMateData(String name, Class<?> javaType, Field field) {
        this.name = name;
        this.javaType = javaType;
        this.field = field;
    }

    /**
     * 构建表列元数据
     * @param table 表名
     * @param name 表字段名
     * @param javaType 表字段类类型
     * @param field 表字段
     */
    public ColumnMateData(String table, String name, Class<?> javaType, Field field) {
        this.table = table;
        this.name = name;
        this.javaType = javaType;
        this.field = field;
    }

    /**
     * 构建表列元数据
     * @param name 表字段名
     * @param remarks 表字段注释
     * @param columnType 表字段类型
     * @param columnSize 表字段大小
     * @param columnDecimalDigits 表字段长度
     * @param nullable 表字段是否可为空
     */
    public ColumnMateData(String name, String remarks, String columnType, int columnSize, int columnDecimalDigits, boolean nullable) {
        this.name = name;
        this.remarks = remarks;
        this.columnType = columnType;
        this.columnSize = columnSize;
        this.columnDecimalDigits = columnDecimalDigits;
        this.nullable = nullable;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public TableMateData getTableMatedata() {
        return tableMatedata;
    }

    public void setTableMatedata(TableMateData tableMatedata) {
        this.tableMatedata = tableMatedata;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getColumnDecimalDigits() {
        return columnDecimalDigits;
    }

    public void setColumnDecimalDigits(int columnDecimalDigits) {
        this.columnDecimalDigits = columnDecimalDigits;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
