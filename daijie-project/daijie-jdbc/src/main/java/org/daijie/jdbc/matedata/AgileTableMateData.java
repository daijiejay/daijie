package org.daijie.jdbc.matedata;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author daijie
 * @since 2019/8/28
 */
public class AgileTableMateData extends TableMatedata {

    private Map<Class, TableMatedata> mateData = Maps.newHashMap();

    public AgileTableMateData(Class entityClass) {
        super(null, entityClass);
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
