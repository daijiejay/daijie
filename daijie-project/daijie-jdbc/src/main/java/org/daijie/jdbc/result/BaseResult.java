package org.daijie.jdbc.result;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.daijie.jdbc.exception.ResultException;
import org.daijie.jdbc.matedata.ColumnMateData;
import org.daijie.jdbc.matedata.MultiTableMateData;
import org.daijie.jdbc.matedata.TableMateData;
import org.daijie.jdbc.matedata.TableMateDataManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 查询结果对象映射基础实现
 * @author daijie
 * @since 2019/6/24
 */
public class BaseResult implements Result {

    private final Logger log = LoggerFactory.getLogger(BaseResult.class);

    /**
     * mapper方法返回对象类型
     */
    private final Class<?> returnClass;

    public BaseResult(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    @Override
    public Object mappingObjectResult(ResultSet resultSet, TableMateData tableMatedata) throws SQLException {
        List<Object> result = new ArrayList<>();
        while (resultSet.next()) {
            if (this.returnClass == Long.class || this.returnClass == long.class) {
                return resultSet.getLong(1);
            } else if (this.returnClass == Integer.class || this.returnClass == int.class) {
                return resultSet.getInt(1);
            } else if (this.returnClass == Float.class || this.returnClass == float.class) {
                return resultSet.getFloat(1);
            } else if (this.returnClass == Short.class || this.returnClass == short.class) {
                return resultSet.getShort(1);
            } else if (this.returnClass == Boolean.class || this.returnClass == boolean.class) {
                return resultSet.getBoolean(1);
            } else if (this.returnClass == Byte.class || this.returnClass == byte.class) {
                return resultSet.getByte(1);
            } else if (this.returnClass == BigDecimal.class) {
                return resultSet.getBigDecimal(1);
            } else if (this.returnClass == String.class) {
                return resultSet.getString(1);
            } else if (this.returnClass == Date.class) {
                return resultSet.getDate(1);
            }
            Object entity = null;
            try {
                if (tableMatedata.getReturnClass() != null) {
                    entity = tableMatedata.getReturnClass().newInstance();
                } else {
                    entity = tableMatedata.getEntityClass().newInstance();
                }
                if (tableMatedata instanceof MultiTableMateData) {
                    mappingObject(resultSet, entity, tableMatedata.getColumns());
                } else {
                    mappingObject(resultSet, entity, tableMatedata.getDefaultColumns());
                }
                result.add(entity);
            } catch (Exception e) {
                throw new ResultException(entity.getClass().getName(), e);
            }
        }
        List<Object> list = null;
        try {
            list = mergeJoinRepeatRows(result);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return getResult(list);
    }

    /**
     * 返回结果映射对象，处理对象中基础类属性和对象属性
     * @param resultSet JDBC执行查询结果对象
     * @param entity 映秀的具体对象
     * @param columns 对象与表对应的字段元数据
     * @throws SQLException 获取JDBC执行查询结果时异常
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     */
    private void mappingObject(ResultSet resultSet, Object entity, Map<String, ColumnMateData> columns) throws SQLException, IllegalAccessException, InstantiationException {
        for (Map.Entry<String, ColumnMateData> entry : columns.entrySet()) {
            entry.getValue().getField().setAccessible(true);
            if (!TableMateDataManage.isBaseClass(entry.getValue().getJavaType())) {
                Object fieldObject = entry.getValue().getJavaType().newInstance();
                TableMateData tableMatedata = entry.getValue().getTableMatedata();
                mappingObject(resultSet, fieldObject, tableMatedata.getColumns());
                if (entry.getValue().getField().getType().isAssignableFrom(List.class)) {
                    List<Object> list = (List<Object>) entry.getValue().getField().get(entity);
                    if (list == null) {
                        list = Lists.newArrayList();
                    }
                    list.add(fieldObject);
                    entry.getValue().getField().set(entity, list);
                } else {
                    entry.getValue().getField().set(entity, fieldObject);
                }
            } else {
                Object value = null;
                if (StringUtils.isNotEmpty(entry.getValue().getTable())) {
                    value = resultSet.getObject(entry.getValue().getTable() + MultiTableMateData.TABLE_COLUMN_FIX + entry.getValue().getName());
                } else {
                    value = resultSet.getObject(entry.getValue().getName());
                }
                if (value != null) {
                    entry.getValue().getField().set(entity, value);
                }
            }
        }
    }

    /**
     * 处理多表关联查询数据合并
     * @param result 查询结果集
     * @return 合并后的结果集
     */
    private List<Object> mergeJoinRepeatRows(List<Object> result) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<Object> list = Lists.newArrayList();
        if (result.isEmpty()) {
            return list;
        }
        for (Object oriObj : result) {
            boolean merge = false;
            for (Object mergeObj : list) {
                if (merge = mergeJoinRepeatRows(mergeObj, oriObj)) {
                    break;
                }
            }
            if (!merge) {
                list.add(oriObj);
            }
        }
        return list;
    }

    /**
     * 处理多表查询重复数据合并到对象
     * @param entity1 第一个对象
     * @param entity2 第二个对象
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws IntrospectionException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     * @return 是否已合并
     */
    private boolean mergeJoinRepeatRows(Object entity1, Object entity2) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        ResultObjectInfo resultObjectInfo1 = getResultObjectInfo(entity1);
        ResultObjectInfo resultObjectInfo2 = getResultObjectInfo(entity2);
        for (Map.Entry<String, List<JoinColumn>> entry : resultObjectInfo1.getPropertiesJoinColumns().entrySet()) {
            String fieldName = entry.getKey();
            Field fieldObjectType = resultObjectInfo2.getProperties().get(fieldName).getField();
            Object fieldObjectValue = resultObjectInfo2.getProperties().get(fieldName).getValue();
            if (fieldObjectValue instanceof List) {
                fieldObjectValue = ((List) fieldObjectValue).get(0);
            }
            ResultObjectInfo childResultObjectInfo = getResultObjectInfo(fieldObjectValue);
            boolean compared = false;
            for (JoinColumn joinColumn : entry.getValue()) {
                Object value1 = resultObjectInfo1.getProperties().get(joinColumn.name()).getValue();
                Object value2 = childResultObjectInfo.getProperties().get(joinColumn.referencedColumnName()).getValue();
                compared = compareParam(value1, value2);
                if (!compared) {
                    return false;
                }
            }
            if (compared) {
                if (fieldObjectType.getType().isAssignableFrom(List.class)) {
                    fieldObjectType.setAccessible(true);
                    List<Object> list1 = (List<Object>) fieldObjectType.get(entity1);
                    List<Object> list2 = (List<Object>) fieldObjectType.get(entity2);
                    list1.addAll(list2);
                    fieldObjectType.set(entity1, list1);
                    return true;
                } else if (!TableMateDataManage.isBaseClass(fieldObjectType.getType())) {
                    return mergeJoinRepeatRows(fieldObjectType.get(entity1), fieldObjectType.get(entity2));
                }
            }
        }
        return false;
    }

    private ResultObjectInfo getResultObjectInfo(Object entity) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        ResultObjectInfo resultObjectInfo = new ResultObjectInfo();
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(entity.getClass(), Object.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Field field = null;
            try {
                field = entity.getClass().getDeclaredField(propertyDescriptor.getName());
            } catch (NoSuchFieldException e) {
            }
            if (field != null) {
                Column column = field.getAnnotation(Column.class);
                String columnName = propertyDescriptor.getName();
                if (column != null && StringUtils.isNotEmpty(column.name())) {
                    columnName = column.name();
                }
                resultObjectInfo.addProperties(columnName, new ResultFieldInfo(field, propertyDescriptor.getReadMethod().invoke(entity)));
                List<JoinColumn> joinColumns = getJoinColumns(field);
                if (!joinColumns.isEmpty()) {
                    resultObjectInfo.addPropertiesJoinColumns(propertyDescriptor.getName(), getJoinColumns(field));
                }
            }
        }
        return resultObjectInfo;
    }

    private List<JoinColumn> getJoinColumns(Field field) {
        List<JoinColumn> list = Lists.newArrayList();
        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
        JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
        if (joinColumns != null) {
            list.addAll(Arrays.asList(joinColumns.value()));
        }
        if (joinColumn != null) {
            list.add(joinColumn);
        }
        return list;
    }

    /**
     * 处理多表查询重复数据合并到对象list中
     * @param result 查询结果集
     * @param entity 映秀的具体对象
     * @param columns 对象与表对应的字段元数据
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws IntrospectionException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     * @return 是否已合并且list中
     */
    private boolean mergeRepeatRows(List<Object> result, Object entity, Map<String, ColumnMateData> columns) throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchFieldException {
        Object obj = null;
        String name = null;
        if (result.isEmpty()) {
            return false;
        }
        for (Object resultEntity : result) {
            compareJoinParams(resultEntity, entity);
            name = compareParams(resultEntity, entity);
            if (name != null) {
                obj = resultEntity;
                break;
            }
        }
        if (obj != null) {
            String[] depthName = name.split("\\.");
            return depthMergeRepeatRows(obj, entity, columns, depthName, 0);
        }
        return false;
    }

    /**
     * 比较两个对象关联字段属性是否相等，且对于不同字段值为LIST对象的字段合并
     * @param entity1 第一个对象
     * @param entity2 第二个对象
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws IntrospectionException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     * @return 返回不等字段名称
     */
    private String compareJoinParams(Object entity1, Object entity2) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        if (entity1.getClass() != entity2.getClass()) {
            return null;
        }
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(entity1.getClass(), Object.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object value1 = propertyDescriptor.getReadMethod().invoke(entity1);
            Object value2 = propertyDescriptor.getReadMethod().invoke(entity2);
            Field field = entity1.getClass().getDeclaredField(propertyDescriptor.getName());
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
            List<JoinColumn> list = Lists.newArrayList();
            if (joinColumns != null) {
                list.addAll(Arrays.asList(joinColumns.value()));
            }
            if (joinColumn != null) {
                list.add(joinColumn);
            }
            if (!list.isEmpty()) {
                for (JoinColumn join : list) {
                    String name = join.name();
                    String referencedColumnName = join.referencedColumnName();

                }
            }
        }
        return null;
    }

    /**
     * 比较两个对象基本属性是否相等，且对于不同字段值为LIST对象的字段合并
     * @param entity1 第一个对象
     * @param entity2 第二个对象
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws IntrospectionException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     * @return 返回不等字段名称
     */
    private String compareParams(Object entity1, Object entity2) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (entity1.getClass() != entity2.getClass()) {
            return null;
        }
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(entity1.getClass(), Object.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object value1 = propertyDescriptor.getReadMethod().invoke(entity1);
            Object value2 = propertyDescriptor.getReadMethod().invoke(entity2);
            if (value1.getClass().isAssignableFrom(byte.class) || value1.getClass().isAssignableFrom(boolean.class) || value1.getClass().isAssignableFrom(char.class) || value1.getClass().isAssignableFrom(short.class)
                    || value1.getClass().isAssignableFrom(long.class) || value1.getClass().isAssignableFrom(double.class) || value1.getClass().isAssignableFrom(int.class) || value1.getClass().isAssignableFrom(float.class)) {
                if (value1 != value2) return propertyDescriptor.getName();
            } else if (value1 instanceof Byte) {
                if (((Byte) value1).byteValue() != ((Byte) value2).byteValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Boolean) {
                if (((Boolean) value1).booleanValue() != ((Boolean) value2).booleanValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Character) {
                if (((Character) value1).charValue() != ((Character) value2).charValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Short) {
                if (((Short) value1).shortValue() != ((Short) value2).shortValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Long) {
                if (((Long) value1).longValue() != ((Long) value2).longValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Double) {
                if (((Double) value1).doubleValue() != ((Double) value2).doubleValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Integer) {
                if (((Integer) value1).intValue() != ((Integer) value2).intValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Float) {
                if (((Float) value1).floatValue() != ((Float) value2).floatValue()) return propertyDescriptor.getName();
            } else if (value1 instanceof Date) {
                if (((Date) value1).getTime() != ((Date) value2).getTime()) return propertyDescriptor.getName();
            } else if (value1 instanceof String) {
                if (!value1.equals(value2)) return propertyDescriptor.getName();
            } else if (value1 instanceof BigDecimal) {
                if (((BigDecimal) value1).compareTo((BigDecimal) value2) != 0) return propertyDescriptor.getName();
            } else if (value1 instanceof List) {
                List<?> list1 = (List<?>) value1;
                List<?> list2 = (List<?>) value2;
                if (!list2.isEmpty()) {
                    for (int i = 0; i < list1.size(); i++) {
                        String name = compareParams(list1.get(i), list2.get(0));
                        if (name != null) return propertyDescriptor.getName() + "." + name;
                    }
                }
            } else {
                if (value2 != null) {
                    String name = compareParams(value1, value2);
                    if (name != null) return propertyDescriptor.getName() + "." + name;
                }
            }
        }
        return null;
    }

    /**
     * 比较两个值是否相等
     * @param value1 第一个值
     * @param value2 第二个值
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws IntrospectionException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     * @return 是否相等
     */
    private boolean compareParam(Object value1, Object value2) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (value1.getClass().isAssignableFrom(byte.class) || value1.getClass().isAssignableFrom(boolean.class) || value1.getClass().isAssignableFrom(char.class) || value1.getClass().isAssignableFrom(short.class)
                || value1.getClass().isAssignableFrom(long.class) || value1.getClass().isAssignableFrom(double.class) || value1.getClass().isAssignableFrom(int.class) || value1.getClass().isAssignableFrom(float.class)) {
            if (value1 != value2) return false;
        } else if (value1 instanceof Byte) {
            if (((Byte) value1).byteValue() != ((Byte) value2).byteValue()) return false;
        } else if (value1 instanceof Boolean) {
            if (((Boolean) value1).booleanValue() != ((Boolean) value2).booleanValue()) return false;
        } else if (value1 instanceof Character) {
            if (((Character) value1).charValue() != ((Character) value2).charValue()) return false;
        } else if (value1 instanceof Short) {
            if (((Short) value1).shortValue() != ((Short) value2).shortValue()) return false;
        } else if (value1 instanceof Long) {
            if (((Long) value1).longValue() != ((Long) value2).longValue()) return false;
        } else if (value1 instanceof Double) {
            if (((Double) value1).doubleValue() != ((Double) value2).doubleValue()) return false;
        } else if (value1 instanceof Integer) {
            if (((Integer) value1).intValue() != ((Integer) value2).intValue()) return false;
        } else if (value1 instanceof Float) {
            if (((Float) value1).floatValue() != ((Float) value2).floatValue()) return false;
        } else if (value1 instanceof Date) {
            if (((Date) value1).getTime() != ((Date) value2).getTime()) return false;
        } else if (value1 instanceof String) {
            if (!value1.equals(value2)) return false;
        } else if (value1 instanceof BigDecimal) {
            if (((BigDecimal) value1).compareTo((BigDecimal) value2) != 0) return false;
        } else if (value1 instanceof List) {
            List<?> list1 = (List<?>) value1;
            List<?> list2 = (List<?>) value2;
            if (!list2.isEmpty()) {
                for (int i = 0; i < list1.size(); i++) {
                    String name = compareParams(list1.get(i), list2.get(0));
                    if (name != null) return false;
                }
            }
        } else {
            if (value2 != null) {
                String name = compareParams(value1, value2);
                if (name != null) return false;
            }
        }
        return true;
    }

    /**
     *
     * @param result 查询结果集
     * @param entity 映秀的具体对象
     * @param columns 对象与表对应的字段元数据
     * @param depthName 查询结果对象的差异字段名
     * @param depthIndex 查询结果对象的差异字段名的深度
     * @return 是否已合并到查询结果集
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     */
    private boolean depthMergeRepeatRows(Object result, Object entity, Map<String, ColumnMateData> columns, String[] depthName, int depthIndex) throws IllegalAccessException {
        for (Map.Entry<String, ColumnMateData> entry : columns.entrySet()) {
            if (entry.getValue().getName().equals(depthName[depthIndex])) {
                if (entry.getValue().getField().getType().isAssignableFrom(List.class)) {
                    List<Object> list1 = (List<Object>) entry.getValue().getField().get(result);
                    List<Object> list2 = (List<Object>) entry.getValue().getField().get(entity);
                    list1.addAll(list2);
                    entry.getValue().getField().set(entity, list1);
                    return true;
                } else if (!TableMateDataManage.isBaseClass(entry.getValue().getJavaType())) {
                    return depthMergeRepeatRows(entry.getValue().getField().get(result), entry.getValue().getField().get(entity), entry.getValue().getTableMatedata().getColumns(), depthName, depthIndex++);
                }
            }
        }
        return false;
    }

    @Override
    public Object getResult(Object result) throws SQLException {
        if (result.getClass() == returnClass
                || (result.getClass() == Integer.class && Integer.TYPE == returnClass)
                || (result.getClass() == Short.class && Short.TYPE == returnClass)
                || (result.getClass() == Double.class && Double.TYPE == returnClass)
                || (result.getClass() == Float.class && Float.TYPE == returnClass)
                || (result.getClass() == Boolean.class && Boolean.TYPE == returnClass)
                || (result.getClass() == Character.class && Character.TYPE == returnClass)
                || (result.getClass() == Byte.class && Byte.TYPE == returnClass)
                || (result.getClass() == Long.class && Long.TYPE == returnClass)) {
            return result;
        }
        List<Object> resultData = null;
        if (result instanceof List) {
            resultData = (List<Object>) result;
        } else if (result instanceof PageResult) {
            resultData = ((PageResult) result).getRows();
        } else {
            resultData = new ArrayList<>();
            resultData.add(result);
        }
        if (this.returnClass == Object.class) {
            if (resultData.size() == 0) {
                return null;
            } else if (resultData.size() == 1) {
                return resultData.get(0);
            } else {
                throw new SqlResultException("查询数据有多条");
            }
        } else if (List.class.isAssignableFrom(returnClass) || PageResult.class.isAssignableFrom(returnClass)) {
            return resultData;
        }
        return null;
    }
}
