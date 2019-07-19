package org.daijie.jdbc.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存信息
 * @author daijie
 * @since 2019/7/14
 */
public class CacheInfo {

    private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap();

    public Object getResultData(String sql) {
        return this.data.get(sql);
    }

    public void setResultData(String sql, Object resultData) {
        this.data.put(sql, resultData);
    }
}
