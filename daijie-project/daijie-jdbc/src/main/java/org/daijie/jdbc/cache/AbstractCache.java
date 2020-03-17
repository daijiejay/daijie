package org.daijie.jdbc.cache;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存操作的简单实现
 * @author daijie
 * @since 2019/7/14
 */
public abstract class AbstractCache implements Cache {

    private ConcurrentHashMap<String, CacheInfo> tableData = new ConcurrentHashMap();

    @Override
    public void set(List<String> tableNames, String sql, Object resultData) {
        String key = joinTableNames(tableNames);
        CacheInfo info = null;
        if (this.tableData.get(key) == null) {
            info = new CacheInfo();
            this.tableData.put(key, info);
        } else {
            info = this.tableData.get(key);
        }
        info.setResultData(sql, resultData);
    }

    @Override
    public Object get(List<String> tableNames, String sql) {
        String key = joinTableNames(tableNames);
        CacheInfo info = null;
        if (this.tableData.get(key) == null) {
            info = new CacheInfo();
            this.tableData.put(key, info);
        } else {
            if (isChangeTable(key)) {
                return null;
            }
            info = this.tableData.get(key);
        }
        return info.getResultData(sql);
    }

    @Override
    public void remove(String tableName) {
        for (String key : this.tableData.keySet()) {
            if (Arrays.asList(key.split(",")).contains(tableName)) {
                this.tableData.remove(key);
            }
        }
    }

    private String joinTableNames(List<String> tableNames) {
        StringJoiner joiner = new StringJoiner(",");
        tableNames.forEach(tableName -> joiner.add(tableName));
        return joiner.toString();
    }
}
