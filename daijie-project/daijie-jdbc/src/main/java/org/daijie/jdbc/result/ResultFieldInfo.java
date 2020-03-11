package org.daijie.jdbc.result;

import java.lang.reflect.Field;

/**
 * 查询结果映射对象字段信息
 *
 * @author daijie
 * @since 2020年03月11日
 */
public class ResultFieldInfo {

    private Field field;

    private Object value;

    public ResultFieldInfo(Field field, Object value) {
        this.field = field;
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
