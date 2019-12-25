package org.daijie.jdbc.result;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.daijie.jdbc.matedata.ColumnMateData;
import org.daijie.jdbc.matedata.MultiTableMateData;
import org.daijie.jdbc.matedata.TableMateData;
import org.daijie.jdbc.matedata.TableMateDataManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 是否多表查询
     */
    private boolean isMulti;

    public BaseResult(Class<?> returnClass, boolean isMulti) {
        this.returnClass = returnClass;
        this.isMulti = isMulti;
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
            try {
                Object entity = null;
                Map<String, ColumnMateData> columns;
                if (tableMatedata.isCostomResult()) {
                    entity = tableMatedata.getReturnClass().newInstance();
                    columns = tableMatedata.getDefaultColumns();
                } else {
                    entity = tableMatedata.getEntityClass().newInstance();
                    columns =  tableMatedata.getColumns();
                }
                mappingObject(resultSet, entity, columns);
                if (!(this.isMulti() && mergeRepeatRows(result, entity, columns))) {
                    result.add(entity);
                }
            } catch (Exception e) {
                log.error("查询结果映射对象失败", e);
            }
        }
        return getResult(result);
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
     * 处理多表查询重复数据合并到对象list中
     * @param result 查询结果集
     * @param entity 映秀的具体对象
     * @param columns 对象与表对应的字段元数据
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws IntrospectionException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     * @return 是否已合并且list中
     */
    private boolean mergeRepeatRows(List<Object> result, Object entity, Map<String, ColumnMateData> columns) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Object obj = null;
        for (Object resultEntity : result) {
            if (compareBasicParams(resultEntity, entity)) obj = resultEntity;
        }
        if (obj != null) {
            for (Map.Entry<String, ColumnMateData> entry : columns.entrySet()) {
                if (entry.getValue().getField().getType().isAssignableFrom(List.class)) {
                    List<Object> list1 = (List<Object>) entry.getValue().getField().get(obj);
                    List<Object> list2 = (List<Object>) entry.getValue().getField().get(entity);
                    list1.addAll(list2);
                    entry.getValue().getField().set(entity, list1);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 比较两个对象基本属性是否相等
     * @param entity1 第一个对象
     * @param entity2 第二个对象
     * @throws IllegalAccessException 将查询结果映射到对象中时异常
     * @throws IntrospectionException 将查询结果映射到对象中时异常
     * @throws InstantiationException 初始化映射对象中具体对象属性时异常
     * @return 返回布尔值
     */
    private boolean compareBasicParams(Object entity1, Object entity2) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (entity1.getClass() != entity2.getClass()) {
            return false;
        }
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(entity1.getClass(), Object.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object value1 = propertyDescriptor.getReadMethod().invoke(entity1);
            Object value2 = propertyDescriptor.getReadMethod().invoke(entity2);
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
            }
        }
        return true;
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

    @Override
    public boolean isMulti() {
        return isMulti;
    }
}
