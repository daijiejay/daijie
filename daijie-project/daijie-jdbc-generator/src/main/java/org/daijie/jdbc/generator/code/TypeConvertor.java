package org.daijie.jdbc.generator.code;

/**
 * 类型转换器
 * @author daijie
 * @since 2019/9/16
 */
public interface TypeConvertor {

    /**
     * 数据库表字段类型转换为JAVA字段类型
     * @param columnType 表字段类型
     * @return 返回JAVA字段类型
     */
    String databaseTypeToJavaType(String columnType);

    /**
     * JAVA字段类型转换为数据库表字段类型
     * @param fieldType JAVA字段类型
     * @return 返回数据库字段类型
     */
    String javaTypeToDatabaseType(String fieldType);
}
