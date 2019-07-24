package org.daijie.jdbc.matedata;

import org.daijie.core.util.ClassInfoUtil;
import org.daijie.jdbc.result.PageResult;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 表元数据初始化管理工具
 * @author daijie
 * @since 2019/5/23
 */
public class TableMatedataManage {

    /**
     * 表字段对应java中的对象类型
     */
    private static Class<?>[] baseClass = new Class<?>[]{byte.class, Byte.class, boolean.class, Boolean.class, char.class, Character.class, short.class, Short.class, long.class, Long.class,
            double.class, Double.class, int.class, Integer.class, float.class, Float.class, Date.class, String.class, BigDecimal.class};

    /**
     * 判断对象是否基本类
     * @return 布尔值
     */
    private static boolean isBaseClass(Class objectClass) {
        for (Class cls : baseClass) {
            if (cls.isAssignableFrom(objectClass)) return true;
        }
        return false;
    }

    /**
     * 表元数据初始化，包括返回对象的无数据映秀字段
     * @param entityClass 表映射对象类型
     * @param returnClass 返回表映射对象类型
     * @param returnClassType 返回表映射对象类型
     * @return TableMatedata 表元数据
     */
    public static TableMatedata initTable(Class entityClass, Class returnClass, Type returnClassType) {
        TableMatedata tableMatedata = null;
        Class realityReturnClass = returnClass;
        if (returnClass.isAssignableFrom(PageResult.class) || returnClass.isAssignableFrom(List.class)) {
            realityReturnClass = ClassInfoUtil.getSuperClassGenricType(returnClassType);
        }
        tableMatedata = initTable(entityClass);
        if (entityClass.isAssignableFrom(realityReturnClass) || realityReturnClass.isAssignableFrom(Object.class) || isBaseClass(realityReturnClass)) {
            return tableMatedata;
        }
        tableMatedata.setReturnClass(realityReturnClass);
        Field[] fields = realityReturnClass.getDeclaredFields();
        for (Field field : fields) {
            String columnName = field.getName();
            ColumnMateData columnMateData = tableMatedata.getColumn(columnName);
            tableMatedata.addResultColumn(columnName, new ColumnMateData(columnMateData.getName(), field.getType(), field));
        }
        return tableMatedata;
    }

    /**
     * 表元数据初始化
     * @param entityClass 表映射对象类型
     * @return TableMatedata 表元数据
     */
    public static TableMatedata initTable(Class entityClass) {
        Table table = (Table) entityClass.getAnnotation(Table.class);
        String tableName = table.name();
        TableMatedata tableMatedata = new TableMatedata(tableName, entityClass);
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            String columnName = field.getName();
            if (column != null) {
                columnName = column.name();
            }
            ColumnMateData columnMateData = new ColumnMateData(columnName, field.getType(), field);
            tableMatedata.addColumn(field.getName(), columnMateData);
            if (field.getAnnotation(Id.class) != null) {
                tableMatedata.setPrimaryKey(columnMateData);
            }
        }
        return tableMatedata;
    }
}
