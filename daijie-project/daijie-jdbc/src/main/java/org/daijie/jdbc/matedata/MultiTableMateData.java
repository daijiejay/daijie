package org.daijie.jdbc.matedata;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

/**
 * @author daijie
 * @since 2019/8/28
 */
public class MultiTableMateData extends TableMatedata {
    public static final String TABLE_COLUMN_FIX = ".";

    /**
     * 整个查询语句中所有用到的表对象映射元数据集合
     */
    private Map<Class, TableMatedata> mateData = Maps.newHashMap();

    public MultiTableMateData(Class entityClass) {
        super(null, entityClass);
        this.setReturnClass(entityClass);
    }

    public Map<Class, TableMatedata> getMateData() {
        return mateData;
    }

    public TableMatedata getMateData(Class entityClass) {
        return mateData.get(entityClass);
    }

    public void addMateData(Class entityClass, TableMatedata mateData) {
        this.mateData.put(entityClass, mateData);
    }

    public void clearMatadata() {
        this.mateData.clear();
    }
}
