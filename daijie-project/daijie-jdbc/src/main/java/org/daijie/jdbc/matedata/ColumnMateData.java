package org.daijie.jdbc.matedata;

import java.lang.reflect.Field;

/**
 * 表列元数据
 * @author daijie
 * @since 2019/5/23
 */
public class ColumnMateData {

    /**
     * 表字段名
     */
    private String name;

    /**
     * 表字段类类型
     */
    private Class<?> javaType;

    /**
     * 表字段
     */
    private Field field;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
