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
public class TableMateDataManage {

    /**
     * 表字段对应java中的对象类型
     */
    private static Class<?>[] baseClass = new Class<?>[]{byte.class, Byte.class, boolean.class, Boolean.class, char.class, Character.class, short.class, Short.class, long.class, Long.class,
            double.class, Double.class, int.class, Integer.class, float.class, Float.class, Date.class, String.class, BigDecimal.class};

    /**
     * 判断对象是否基本类
     * @param objectClass 具体对象
     * @return 布尔值
     */
    public static boolean isBaseClass(Class objectClass) {
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
    public static TableMateData initTable(Class entityClass, Class returnClass, Type returnClassType) {
        TableMateData tableMatedata = null;
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
    public static TableMateData initTable(Class entityClass) {
        String tableName = null;
        Object annotation = entityClass.getAnnotation(Table.class);
        if (annotation instanceof Table) {
            Table table = (Table) entityClass.getAnnotation(Table.class);
            tableName = table.name();
        }
        TableMateData tableMatedata = new TableMateData(tableName, "", entityClass);
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
        initMultiTableColumnMataData(tableMatedata, entityClass, null, true);
        return tableMatedata;
    }

    /**
     * 深度遍历初始化表对象映射属性
     * @param tableMatedata 多表元数据
     * @param entityClass 表映射对象类型
     * @param childrenTableMatedata 子表元数据
     * @param isRoot 是否根节点
     */
    private static void initMultiTableColumnMataData(MultiTableMateData tableMatedata, Class entityClass, MultiTableMateData childrenTableMatedata, boolean isRoot) {
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
            Class fieldClass = field.getType();
            if (column != null) {
                if (StringUtils.isNotEmpty(column.table())) {
                    columnTable = column.table();
                }
                if (StringUtils.isNotEmpty(column.name())) {
                    columnName = column.name();
                }
            }
            if (!isBaseClass(fieldClass)) {
                if (field.getType().isAssignableFrom(List.class)) {
                    fieldClass = ClassInfoUtil.getSuperClassGenricType(field.getGenericType());
                } else {
                    fieldClass = field.getDeclaringClass();
                }
                childrenTableMatedata = new MultiTableMateData(fieldClass);
                initMultiTableColumnMataData(tableMatedata, fieldClass, childrenTableMatedata, false);
            }
            ColumnMateData columnMateData = new ColumnMateData(columnTable, columnName, fieldClass, field);
            columnMateData.setTableMatedata(childrenTableMatedata);
            if (isRoot) {
                tableMatedata.addColumn(field.getName(), columnMateData);
            } else {
                childrenTableMatedata.addColumn(field.getName(), columnMateData);
            }
            if (isBaseClass(fieldClass)) {
                tableMatedata.addResultColumn(columnTable + MultiTableMateData.TABLE_COLUMN_FIX + field.getName(), columnMateData);
            }
        }
    }

    /**
     * 表元数据初始化
     * @param returnEntityClass 表映射对象类型
     * @param multiWrapper 多表条件包装
     * @return TableMatedata 表元数据
     */
    public static MultiTableMateData initTable(Class returnEntityClass, MultiWrapper multiWrapper) {
        MultiTableMateData agileTableMateData = TableMateDataManage.initMultiTable(returnEntityClass);
        Set<Class> entityClasses = multiWrapper.getEntityClasses();
        for (Class entityClass : entityClasses) {
            TableMateData matedata = TableMateDataManage.initTable(entityClass);
            agileTableMateData.addMateData(matedata.getEntityClass(), matedata);
        }
        return agileTableMateData;
    }
}
