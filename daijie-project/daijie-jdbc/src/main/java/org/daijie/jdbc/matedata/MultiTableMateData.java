package org.daijie.jdbc.matedata;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 多表关联查询表元数据属性类
 * @author daijie
 * @since 2019/8/28
 */
public class MultiTableMateData extends TableMateData {

    /**
     * 多表关联查询表名与字段名引用的符号
     */
    public static final String TABLE_COLUMN_FIX = ".";

    /**
     * 整个查询语句中所有用到的表对象映射元数据集合
     */
    private Map<Class, TableMateData> mateData = Maps.newHashMap();

    /**
     * 构建多表关联查询表元数据属性类
     * @param entityClass 多表关联查询返回映射对象
     */
    public MultiTableMateData(Class entityClass) {
        super(entityClass.getName(), entityClass);
        this.setReturnClass(entityClass);
    }

    public Map<Class, TableMateData> getMateData() {
        return mateData;
    }

    public TableMateData getMateData(Class entityClass) {
        return mateData.get(entityClass);
    }

    public void addMateData(Class entityClass, TableMateData mateData) {
        this.mateData.put(entityClass, mateData);
    }

    public void clearMatadata() {
        this.mateData.clear();
    }
}
