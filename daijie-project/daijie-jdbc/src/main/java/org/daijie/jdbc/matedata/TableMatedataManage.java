package org.daijie.jdbc.matedata;

import org.apache.commons.lang3.StringUtils;
import org.daijie.core.util.ClassInfoUtil;
import org.daijie.jdbc.result.PageResult;
import org.daijie.jdbc.scripting.MultiWrapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
        String tableName = null;
        Object annotation = entityClass.getAnnotation(Table.class);
        if (annotation instanceof Table) {
            Table table = (Table) entityClass.getAnnotation(Table.class);
            tableName = table.name();
        }
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

    /**
     * 多个表元数据初始化
     * @param entityClass 表映射对象类型
     * @return TableMatedata 多表元数据
     */
    public static MultiTableMateData initMultiTable(Class entityClass) {
        MultiTableMateData tableMatedata = new MultiTableMateData(entityClass);
        initMultiTableColumnMataData(tableMatedata, entityClass);
        return tableMatedata;
    }

    /**
     * 深度遍历初始化表对象映射属性
     * @param tableMatedata 多表元数据
     * @param entityClass 表映射对象类型
     */
    private static void initMultiTableColumnMataData(MultiTableMateData tableMatedata, Class entityClass) {
        String tableName = null;
        Object annotation = entityClass.getAnnotation(Table.class);
        if (annotation instanceof Table) {
            Table table = (Table) entityClass.getAnnotation(Table.class);
            tableName = table.name();
        }
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            String columnName = field.getName();
            String columnTable = tableName;
            if (column != null) {
                if (StringUtils.isNotEmpty(column.table())) {
                    columnTable = column.table();
                }
                if (StringUtils.isNotEmpty(column.name())) {
                    columnName = column.name();
                }
                if (!isBaseClass(field.getType())) {
                    Class fieldClass;
                    if (field.getType().isAssignableFrom(List.class)) {
                        fieldClass = ClassInfoUtil.getSuperClassGenricType(field.getGenericType());
                    } else {
                        fieldClass = field.getDeclaringClass();
                    }
                    initMultiTableColumnMataData(tableMatedata, fieldClass);
                    break;
                }
            }
            ColumnMateData columnMateData = new ColumnMateData(columnTable, columnName, field.getType(), field);
            tableMatedata.addResultColumn(columnTable + MultiTableMateData.TABLE_COLUMN_FIX + field.getName(), columnMateData);
        }
    }

    /**
     * 表元数据初始化
     * @param returnEntityClass 表映射对象类型
     * @return TableMatedata 表元数据
     */
    public static MultiTableMateData initTable(Class returnEntityClass, MultiWrapper agileWrapper) {
        Set<Class> entityClasses = agileWrapper.getEntityClasses();
        MultiTableMateData agileTableMateData = TableMatedataManage.initMultiTable(returnEntityClass);
        for (Class entityClass : entityClasses) {
            TableMatedata matedata = TableMatedataManage.initTable(entityClass);
            agileTableMateData.addMateData(matedata.getEntityClass(), matedata);
        }
        return agileTableMateData;
    }
}
