package org.daijie.jdbc.generator.code;

/**
 * MYSQL数据库类型转换器
 * @author daijie
 * @since 2019/9/16
 */
public class MysqlTypeConvertor implements TypeConvertor {

    @Override
    public String databaseTypeToJavaType(String columnType) {
        switch (columnType.toLowerCase()){
            case "tinyint": return "java.lang.Integer";
            case "smallint": return "java.lang.Integer";
            case "mediumint": return "java.lang.Integer";
            case "int": return "java.lang.Integer";
            case "integer": return "java.lang.Integer";
            case "bigint": return "java.lang.Long";
            case "bit": return "java.lang.Boolean";
            case "float": return "java.math.BigDecimal";
            case "double": return "java.math.BigDecimal";
            case "decimal": return "java.math.BigDecimal";
            case "date": return "java.util.Date";
            case "time": return "java.util.Date";
            case "year": return "java.util.Date";
            case "datetime": return "java.util.Date";
            case "timestamp": return "java.util.Date";
            case "char": return "java.lang.String";
            case "varchar": return "java.lang.String";
            case "tinyblob": return "java.lang.String";
            case "tinytext": return "java.lang.String";
            case "blob": return "java.lang.String";
            case "text": return "java.lang.String";
            case "mediumblob": return "java.lang.String";
            case "mediumtext": return "java.lang.String";
            case "longblob": return "java.lang.String";
            case "longtext": return "java.lang.String";
            default: return "java.lang.String";
        }
    }

    @Override
    public String javaTypeToDatabaseType(String fieldType) {
        switch (fieldType){

        }
        return null;
    }
}
