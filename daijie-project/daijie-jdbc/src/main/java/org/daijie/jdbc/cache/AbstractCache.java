package org.daijie.jdbc.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存操作的简单实现
 * @author daijie
 * @since 2019/7/14
 */
public abstract class AbstractCache implements Cache {

    private ConcurrentHashMap<String, CacheInfo> tableData = new ConcurrentHashMap();

    @Override
    public void set(String tableName, String sql, Object resultData) {
        CacheInfo info = null;
        if (this.tableData.get(tableName) == null) {
            info = new CacheInfo();
            this.tableData.put(tableName, info);
        } else {
            info = this.tableData.get(tableName);
        }
        info.setResultData(sql, resultData);
    }

    @Override
    public Object get(String tableName, String sql) {
        CacheInfo info = null;
        if (this.tableData.get(tableName) == null) {
            info = new CacheInfo();
            this.tableData.put(tableName, info);
        } else {
            if (isChangeTable(tableName)) {
                return null;
            }
            info = this.tableData.get(tableName);
        }
        return info.getResultData(sql);
    }

    @Override
    public void remove(String tableName) {
        this.tableData.remove(tableName);
    }
}
