package org.daijie.jdbc.matedata;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 表元数据初始化管理工具
 * @author daijie
 * @since 2019/5/23
 */
public class TableMatedataManage {

    /**
     * 表字段对应java中的对象类型
     */
    private static Class<?>[] baseClass = new Class<?>[]{Byte.class, Boolean.class, Character.class, Short.class, Long.class,
            Double.class, Integer.class, Float.class, Date.class, String.class, BigDecimal.class};

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
