package org.daijie.jdbc.result;

import org.daijie.jdbc.matedata.ColumnMateData;
import org.daijie.jdbc.matedata.TableMatedata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询结果对象映射基础实现
 * @author daijie
 * @since 2019/6/24
 */
public class BaseResult implements Result {

    /**
     * mapper方法返回对象类型
     */
    private final Class<?> returnClass;

    public BaseResult(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    @Override
    public Object mappingObjectResult(ResultSet resultSet, TableMatedata tableMatedata) throws SQLException {
        List<Object> result = new ArrayList<>();
        while (resultSet.next()) {
            if (this.returnClass == Long.class || this.returnClass == long.class) {
                return resultSet.getLong(1);
            } else if (this.returnClass == Integer.class || this.returnClass == int.class) {
                return resultSet.getInt(1);
            }
            try {
                Object entity = null;
                Map<String, ColumnMateData> columns;
                if (tableMatedata.isCostomResult()) {
                    entity = tableMatedata.getReturnClass().newInstance();
                    columns = tableMatedata.getResultColumns();
                } else {
                    entity = tableMatedata.getEntityClass().newInstance();
                    columns =  tableMatedata.getColumns();
                }
                for (Map.Entry<String, ColumnMateData> entry : columns.entrySet()) {
                    entry.getValue().getField().setAccessible(true);
                    entry.getValue().getField().set(entity, resultSet.getObject(entry.getValue().getName()));
                }
                result.add(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getResult(result);
    }

    @Override
    public Object getResult(Object result) throws SQLException {
        if (result.getClass() == returnClass) {
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

    private int getIntResutl(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(1);
    }

    private long getLongResutl(ResultSet resultSet) throws SQLException {
        return resultSet.getLong(1);
    }

    private List<Object> getMateDateResutl(ResultSet resultSet, TableMatedata tableMatedata) throws SQLException {
        List<Object> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Object entity = tableMatedata.getEntityClass().newInstance();
                for (Map.Entry<String, ColumnMateData> entry : tableMatedata.getColumns().entrySet()) {
                    entry.getValue().getField().setAccessible(true);
                    entry.getValue().getField().set(entity, resultSet.getObject(entry.getValue().getName()));
                }
                result.add(entity);
            }
        } catch (IllegalAccessException illegalAccessException) {
            throw new SqlResultException(tableMatedata.getEntityClass().getName() + "映射属性值失败", illegalAccessException);
        } catch (InstantiationException instantiationException) {
            throw new SqlResultException(tableMatedata.getEntityClass().getName() + "没有无参构造方法", instantiationException);
        }
        return result;
    }

}
